/*
 * Copyright 2021 TTT.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ttt.tamagolem.game.logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import ttt.tamagolem.contents.exceptions.UnitializedException;
import ttt.tamagolem.contents.graph.Node;
import ttt.tamagolem.contents.structure.Player;
import ttt.tamagolem.contents.structure.balance.Balance;
import ttt.tamagolem.contents.structure.golem.Golem;
import ttt.tamagolem.contents.xml.elements.ElementoSecondario;
import ttt.tamagolem.contents.xml.elements.Nome;
import ttt.tamagolem.game.MainMenu.Difficulty;
import ttt.tamagolem.game.logic.commons.CommonRocksetStore;
import ttt.tamagolem.game.support.Broadcast;
import ttt.tamagolem.game.support.PhrasePicker;
import ttt.utils.console.input.ConsoleInput;
import ttt.utils.console.menu.Menu;
import ttt.utils.console.menu.utils.FutureAction;
import ttt.utils.console.menu.utils.Pair;
import ttt.utils.console.output.GeneralFormatter;

/**
 * Gestisce un gioco.
 *
 * @author TTT
 */
public class GameHandler {

    private final Player p1;
    private final Player p2;
    private ArrayList<String> nodes_names;
    private Difficulty diff;
    private Balance b;
    private StartValueCalculator svc;
    private CommonRocksetStore crs;
    private BattleHandler battle_handler;

    private boolean finished = false;
    private GameStates current = GameStates.VOID;

    public GameHandler(Player p1, Player p2) {
        this.p1 = p1;
        this.p2 = p2;
        init();
    }

    private void init() {
        Broadcast.broadcastGameState(Broadcast.LOSER_PLAYER, null, this);
        Broadcast.broadcastGameState(Broadcast.WINNER_PLAYER, null, this);
        Broadcast.broadcastGameState(Broadcast.CURRENT_GAME_HANDLER, this, this);
    }

    /**
     * Avvia la partita e avverte tutte le istanze in ascolto.
     *
     * @throws ttt.tamagolem.contents.exceptions.UnitializedException Quando la
     * partita non è ancora stata impostata con i valori calcolati.
     */
    public void start() throws UnitializedException {
        //Prendo i valori comuni al gioco.
        diff = (Difficulty) Broadcast.askForGameState(Broadcast.CURRENT_GAME_DIFFICULTY);
        Nome n = (Nome) Broadcast.askForGameState(Broadcast.GAME_NODE_NAMES);
        //Genero i nomi dei nodi
        nodes_names = new ArrayList<>();
        ArrayList<String> tmp = new ArrayList<>();
        n.getElements().forEach(iel -> {
            ElementoSecondario es = (ElementoSecondario) iel;
            tmp.add(es.getValue());
        });
        Collections.shuffle(tmp);
        Random r = new Random(new Date().getTime());
        int maxNodes = r.nextInt(diff.getMax() - diff.getMin() + 1) + diff.getMin();
        Broadcast.broadcastGameValue(Broadcast.NODES_COUNT, maxNodes);
        for (int i = 0; i < maxNodes; i++) {
            nodes_names.add(tmp.get(i));
        }
        //Inizializzo l'equilibrio
        b = new Balance(5 * diff.getMin(), nodes_names);
        b.generateNodeInteractions();
        b.generateLinkValues();
        //Eseguo i calcoli
        svc = new StartValueCalculator(b);
        diff.exec();
        //Creo lo Store di pietre comuni
        crs = CommonRocksetStore.getInstance();
        crs.setup(b);
        //Prima di annunciare l'inizio partita
        current = GameStates.STARTED;
        Broadcast.forceBroadcastGameState(this);
        //Dopo averlo annunciato.
        GeneralFormatter.incrementIndents();
        //Creo golem per giocatore 1
        generateGolem(p1);
        System.out.println();
        System.out.println();
        //Creo golem per giocatore 2
        generateGolem(p2);
        GeneralFormatter.decrementIndents();
        //Inizio la gestione della battaglia
        battle_handler = new BattleHandler(p1, p2);
        current = GameStates.NEXT_ROUND;
        Broadcast.forceBroadcastGameState(this);
        battle_handler.rockBattle();
    }

    /**
     * Genera un golem per un giocatore, questo metodo effettua il controllo per
     * determinare su una partita è finita.
     *
     * @param player Il giocatore per cui generare il golem.
     */
    private void generateGolem(Player player) {
        if (player != null) {
            Golem g = player.getGolem();
            if (g == null || g.isDead()) {
                current = GameStates.GOLEM_GENERATION;
                if (player.getUsedGolems() < Broadcast.askForGameValue(Broadcast.MAX_GOLEM_AMOUNT)) {
                    try {
                        GeneralFormatter.printOut(PhrasePicker.getInstance().getGenerationString(player), true, false);
                        GeneralFormatter.printOut("[ GENERA GOLEM PER: " + player.getName() + " ]", true, false);
                        System.out.println();
                        GeneralFormatter.incrementIndents();
                        generateRockset();
                        GeneralFormatter.decrementIndents();
                        Golem opponents_golem = getOpponent(player).getGolem();
                        while (crs.previewOrder().allSame() // controlla se tutte le pietre dei giocatori sono esattamente le stesse (poi ci siamo accorti che non è un caso possibile)
                                && (opponents_golem != null ? crs.previewOrder().equals(opponents_golem.getRockset()) : false)) {
                            crs.deleteOrder();
                            GeneralFormatter.incrementIndents();
                            GeneralFormatter.printOut("Le pietre corrispondono a quelle del golem avversario e sono tutte uguali: rifare il set.", true, false);
                            GeneralFormatter.decrementIndents();
                            generateRockset();
                        }
                        g = new Golem(Broadcast.askForGameValue(Broadcast.MAX_GOLEM_LIFE), crs.flushOrder());
                        player.setGolem(g);
                        Broadcast.forceBroadcastGameState(this);

                        if (opponents_golem != null) { // se le pietre dei due giocatori sono le stesse nella stessa sequenza va a cambiare la sequenza del secondo giocatore
                            if (opponents_golem.getRockset().equals(g.getRockset())) {
                                g.getRockset().addOffset(1);
                                GeneralFormatter.incrementIndents();
                                GeneralFormatter.printOut("Le pietre corrispondono a quelle del golem avversario: sono stato spostate di un ciclo.", true, false);
                                GeneralFormatter.decrementIndents();
                            }
                        }
                        g.addListener(() -> {
                            GeneralFormatter.incrementIndents();
                            GeneralFormatter.printOut("Il golem di " + player.getName() + " è morto...", true, false);
                            GeneralFormatter.decrementIndents();
                            generateGolem(player);
                        });
                        if (battle_handler != null) {
                            current = GameStates.NEXT_ROUND;
                            Broadcast.forceBroadcastGameState(this);
                            battle_handler.rockBattle();
                        }
                    } catch (UnitializedException ex) {
                        Logger.getLogger(GameHandler.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else { //Questo giocatore ha perso
                    crs.closeSet();
                    GeneralFormatter.printOut("Il giocatore " + player.getName() + " ha finito i golem...", true, false);
                    Broadcast.broadcastGameState(Broadcast.GAME_BALANCE, b, this);
                    Broadcast.broadcastGameState(Broadcast.LOSER_PLAYER, player, this);
                    Broadcast.broadcastGameState(Broadcast.WINNER_PLAYER, getOpponent(player), this);
                    current = GameStates.FINISHED;
                    Broadcast.forceBroadcastGameState(this);
                }
            }
        }
    }

    //<editor-fold defaultstate="collapsed" desc="Gestione pietre">
    private Pair<String, FutureAction<Void>> complete_rockset;
    Integer max_per_golem;

    /**
     * Richiede di ordinare dalla scorta comune un set di pietre.
     */
    public void generateRockset() {
        max_per_golem = Broadcast.askForGameValue(Broadcast.MAX_ROCKS_PER_GOLEM);
        Menu<Void> generator = new Menu<>("{Scegli le pietre che guideranno il tuo golem alla vittoria}") {
        };
        generator.removeOption(1);
        generator.autoPrintSpaces(false);
        complete_rockset = new Pair<>("Conferma set", () -> {

            generator.quit();
            return null;
        });
        for (int i = 0; i < max_per_golem; i++) {
            String title = "Imposta Pietra " + (i + 1);
            addOptionLazy(generator, title, i + 1);
        }
        generator.paintMenu();
    }

    /**
     * Aggiunge un'opzione per rimuovere dall'ordine effettuato una pietra.
     *
     * @param m Il menù su cui operare.
     * @param n Il nodo scelto.
     * @param title Il titolo dell'opzione di selezione.
     * @param pos La posizione della voce nel menù.
     */
    private void removeOptionLazy(Menu m, Node n, String title, int pos) {
        String title_remove = "Rimuovi " + n.getName();
        m.addOption(pos, title_remove, () -> {
            crs.removePreorder(n);
            m.removeOption(m.optionLookup(title_remove) + 1);
            addOptionLazy(m, title, pos);
            if (crs.getOrderSize() < max_per_golem) {
                m.removeOption(complete_rockset);
            }
            return null;
        });
    }

    /**
     * Aggiunge un'opzione per ordinare una pietra dalla scorta comune.
     *
     * @param m Il menù su cui operare.
     * @param title Il titolo dell'opzione di selezione.
     * @param pos La posizione della voce nel menù.
     */
    private void addOptionLazy(Menu m, String title, int pos) {
        m.addOption(pos, title, () -> {
            GeneralFormatter.incrementIndents();
            Node n = selectRock("Pietra " + pos);
            GeneralFormatter.decrementIndents();
            m.removeOption(m.optionLookup(title) + 1);
            removeOptionLazy(m, n, title, pos);
            if (crs.getOrderSize() == max_per_golem) {
                m.addOption(complete_rockset);
            }
            return null;
        });
    }

    /**
     * Selezione di una pietra.
     *
     * @param title Il titolo da mostrare.
     * @return Il tipo di pietra scelto.
     */
    private Node selectRock(String title) {
        Menu<Node> m = new Menu<>("{Cambia " + title + "}") {
        };
        m.removeOption(1);
        m.autoPrintSpaces(false);
        crs.getAvailableElements().forEach(n -> {
            m.addOption(n.getName() + " (rimaste: " + crs.getRocksLeftCount(n) + ")", () -> {
                crs.preorder(n);
                return n;
            });
        });
        return m.showAndWait();
    }
//</editor-fold>

    /**
     * Ritorna l'opponente di un giocatore.
     *
     * @param p Il giocatore per cui trovare l'opponente.
     * @return Il giocatore opponente.
     */
    private Player getOpponent(Player p) {
        return p == p1 ? p2 : p1;
    }

    /**
     * Controlla la partita è terminate.
     *
     * @return {@code true} se è terminata.
     */
    public boolean isFinished() {
        return finished;
    }

    /**
     * Chiede l'abbandono forzato all'utente (prima che la partita sia
     * terminata).
     *
     * @return La scelta dell'utente.
     */
    public boolean rageQuit() {
        //Calcola pseudo-vincitore (chi ha ancora più golems)
        boolean to_quit = ConsoleInput.getInstance().readBoolean("Vuoi abbandonare la partita in corso?");
        finished = to_quit;
        if (to_quit) {
            //mostra pseudo-risultato
            //Azzera tutto
            Broadcast.resetAll();
        }
        return to_quit;
    }

    /**
     * Ritorna lo stato di gioco corrente.
     *
     * @return Lo stato del gioco.
     */
    public GameStates getCurrentState() {
        return current;
    }

}
