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
package tamagolem.game;

import java.util.logging.Level;
import java.util.logging.Logger;
import tamagolem.contents.exceptions.UnitializedException;
import tamagolem.contents.structure.balance.Balance;
import tamagolem.game.support.ReadXML;
import tamagolem.contents.structure.Player;
import tamagolem.contents.xml.elements.ElementoPrincipale;
import tamagolem.contents.xml.elements.Nome;
import tamagolem.game.logic.GameHandler;
import tamagolem.game.logic.GameStates;
import tamagolem.game.support.Broadcast;
import tamagolem.game.support.broadcast.GameStateListener;
import ttt.utils.ProjectSettings;
import ttt.utils.console.input.ObjectInputEngine;
import ttt.utils.console.menu.Menu;
import ttt.utils.console.menu.utils.FutureAction;
import ttt.utils.console.output.GeneralFormatter;
import ttt.utils.xml.engine.interfaces.IXMLElement;

/**
 * Menù principale del gioco.
 *
 * @author TTT
 */
public class MainMenu {

    private final Menu<Void> main;
    private Difficulty difficolta;
    private ElementoPrincipale elenco_nomi_disponibili;
    private Nome elenco_nomi_nodi;
    private GameHandler gm = null;

    /**
     * Le difficoltà possibili, alcune possono anche modificare le impostazioni
     * di gioco.
     */
    public static enum Difficulty {
        FACILE(3, 5, () -> {
            return null;
        }), NORMALE(6, 8, () -> {
            return null;
        }), DIFFICILE(9, 10, () -> {
            return null;
        }), HARDCORE(9, 10, () -> {
            Broadcast.broadcastGameValue(Broadcast.MAX_GOLEM_AMOUNT, Broadcast.askForGameValue(Broadcast.MAX_GOLEM_AMOUNT) - 2);
            return null;
        }), ESTREMA(11, 12, () -> {
            Broadcast.broadcastGameValue(Broadcast.MAX_GOLEM_AMOUNT, Broadcast.askForGameValue(Broadcast.MAX_GOLEM_AMOUNT) - 4);
            return null;
        });
        int min;
        int max;
        FutureAction<Void> act;

        private Difficulty(int min, int max, FutureAction<Void> act) {
            this.min = min;
            this.max = max;
            this.act = act;
        }

        public int getMin() {
            return min;
        }

        public int getMax() {
            return max;
        }

        public void exec() {
            act.onSelected();
        }

        @Override
        public String toString() {
            return this.name().substring(0, 1).toUpperCase() + this.name().substring(1).toLowerCase();
        }

    }

    public MainMenu() {
        this.difficolta = Difficulty.NORMALE;
        main = new Menu<>("TAMA-GOLEM") {
        };
        init();
    }

    private Player player1;
    private Player player2;
    private boolean op_ = true;

    private void init() {
        ProjectSettings.PROGRAM_DEFAULT_OBJECT_OUTPUT_PHRASE = false;
        ProjectSettings.PROGRAM_SHOW_OBJECT_INPUT = false;
        main.addOption("Impostazioni", () -> {
            GeneralFormatter.incrementIndents();
            showSettings();
            GeneralFormatter.decrementIndents();
            return null;
        });
        main.addOption("Imposta giocatori", () -> {
            GeneralFormatter.incrementIndents();
            setPlayers();
            GeneralFormatter.decrementIndents();
            if (op_) {
                op_ = false;
                main.addLazyExecutable(() -> {
                    main.addOption("Inizia una nuova partita", () -> {
                        try {
//                            if (gm != null
//                                    && (gm.getCurrentState().ordinal() > GameStates.VOID.ordinal()
//                                    && gm.getCurrentState().ordinal() <= GameStates.FINISHED.ordinal())
//                                    && !gm.isFinished()) {
//                                if (!gm.rageQuit()) {
//                                    return null;
//                                } else {
//                                    player1 = new Player(player1.getName());
//                                    player2 = new Player(player2.getName());
//                                    gm = new GameHandler(player1, player2);
//                                }
//                            }

                            player1.reset();
                            player2.reset();
                            gm = new GameHandler(player1, player2);

                            Broadcast.broadcastGameState(Broadcast.CURRENT_GAME_DIFFICULTY, difficolta, gm);
                            Broadcast.broadcastGameState(Broadcast.GAME_NODES, elenco_nomi_nodi, gm);
                            gm.start();
                        } catch (UnitializedException ex) {
                            Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        return null;
                    });
                    return null;
                });
            }
            return null;
        });
        //Caricare file XML e impostare valori default.
        elenco_nomi_disponibili = (ElementoPrincipale) ReadXML.loadNomi().getFirstElement("elementi");
        elenco_nomi_nodi = (Nome) elenco_nomi_disponibili.getFirstElement("nomi");
        initListeners();
        main.setDefaultSpaces(1);
        main.paintMenu();
    }

    private void initListeners() {
        Broadcast.addGameStateListener(new GameStateListener() {
            @Override
            public void onStart(GameHandler gh) {
                GeneralFormatter.printOut("Sfidanti, prepararsi per la battaglia!", true, false);
                printOutVals();
            }

            @Override
            public void onNextRound(GameHandler gh) {
                GeneralFormatter.printOut("I golem stanno iniziando a combattere!", true, false);
                main.consoleSpaces(2);
            }

            @Override
            public void onFinish(GameHandler gh) {
                GeneralFormatter.printOut("La battaglia e' conclusa!", true, false);
                printEndBattle();
            }
        });
    }

    private void printOutVals() {
        System.out.println();
        GeneralFormatter.printOut("In quest partita sono state disposte le seguenti regole:", true, false);
        GeneralFormatter.incrementIndents();
        GeneralFormatter.printOut("- Difficolta'                       : " + difficolta.toString(), true, false);
        GeneralFormatter.printOut("- Vita massima di un golem          : " + Broadcast.askForGameValue(Broadcast.MAX_GOLEM_LIFE), true, false);
        GeneralFormatter.printOut("- Numero elementi disponibili       : " + Broadcast.askForGameValue(Broadcast.GAME_NODES), true, false);
        GeneralFormatter.printOut("- Numero massimo di golem giocabili : " + Broadcast.askForGameValue(Broadcast.MAX_GOLEM_AMOUNT), true, false);
        GeneralFormatter.printOut("- Numero massimo di pietre comuni   : " + Broadcast.askForGameValue(Broadcast.COMMON_ROCKS_AVAILABLE), true, false);
        GeneralFormatter.printOut("- Numero massimo di pietre per golem: " + Broadcast.askForGameValue(Broadcast.MAX_ROCKS_PER_GOLEM), true, false);
        GeneralFormatter.decrementIndents();
        System.out.println();
        System.out.println();
    }

    /**
     * Mostra il menù delle impostazioni del programma.
     */
    private void showSettings() {
        Menu<Void> impostazioni = new Menu<Void>("{Impostazioni}") {
        };
        impostazioni.removeOption(1);
        impostazioni.autoPrintSpaces(false);
        impostazioni.addOption("Lista elementi", () -> {
            GeneralFormatter.incrementIndents();
            selectElements();
            GeneralFormatter.decrementIndents();
            impostazioni.consoleSpaces(1);
            return null;
        });
        impostazioni.addOption("Difficolta'", () -> {
            GeneralFormatter.incrementIndents();
            selectDifficulty();
            GeneralFormatter.decrementIndents();
            impostazioni.consoleSpaces(1);
            return null;
        });
        impostazioni.addOption("Indietro", () -> {
            impostazioni.quit();
            return null;
        });
        impostazioni.paintMenu();
    }

    /**
     * Mostra il menù per la selezione della difficoltà.
     */
    private void selectDifficulty() {
        Menu<Difficulty> difficolta_menu = new Menu<>("{Seleziona difficoltà : " + difficolta.toString() + "}") {
        };
        difficolta_menu.removeOption(1);
        difficolta_menu.autoPrintSpaces(false);
        for (Difficulty d : Difficulty.values()) {//[FACILE,NORMALE,DIFFICILE,...]
            difficolta_menu.addOption(d.toString(), () -> {
                this.difficolta = d;
                GeneralFormatter.printOut("Difficolta' impostata a: " + d.toString(), true, false);
                difficolta_menu.quit();
                return d;
            });
        }
        difficolta_menu.addOption("Indietro", () -> {
            difficolta_menu.quit();
            return difficolta;
        });
        difficolta = difficolta_menu.showAndWait();
    }

    /**
     * Mostra il menù per la selezione dei nomi degli elementi.
     */
    private void selectElements() {
        //Selezione degli elementi da quelli letti nel file XML
        Menu<Nome> selezione_nomi_elementi = new Menu<>("{Scegli nome elementi : " + elenco_nomi_nodi.getType() + "}") {
        };
        selezione_nomi_elementi.removeOption(1);
        selezione_nomi_elementi.autoPrintSpaces(false);
        for (IXMLElement nome_elemento : elenco_nomi_disponibili.getElements()) {
            Nome opzione = (Nome) nome_elemento;
            selezione_nomi_elementi.addOption(opzione.getType(), () -> {
                GeneralFormatter.printOut("I nomi degli elementi sono presi dalla lista:" + opzione.getType(), true, false);
                selezione_nomi_elementi.quit();
                return opzione;
            });
        }
        selezione_nomi_elementi.addOption("Indietro", () -> {
            selezione_nomi_elementi.quit();
            return elenco_nomi_nodi;
        });
        this.elenco_nomi_nodi = selezione_nomi_elementi.showAndWait();
    }

    /**
     * Imposta i giocatori.
     */
    private void setPlayers() {
        player1 = ObjectInputEngine.readNewObject(Player.class, "Imposta giocatore 1");
        player2 = ObjectInputEngine.readNewObject(Player.class, "Imposta giocatore 2");
        if (gm != null
                && (gm.getCurrentState().ordinal() > GameStates.VOID.ordinal()
                && gm.getCurrentState().ordinal() < GameStates.FINISHED.ordinal())
                && !gm.isFinished()) {
            if (!gm.rageQuit()) {
                return;
            }
        }
        Broadcast.forceBroadcastGameState(gm);
        gm = new GameHandler(player1, player2);
    }

    private void printEndBattle(){
        Player winner = (Player) Broadcast.askForGameState(Broadcast.WINNER_PLAYER);
        Player loser = (Player) Broadcast.askForGameState(Broadcast.LOSER_PLAYER);
        Balance b = (Balance) Broadcast.askForGameState(Broadcast.GAME_BALANCE);
        GeneralFormatter.printOut("Il giocatore " + winner.getName() + " ha sconfitto il suo avversario " + loser.getName() + "!", true,false);
        main.consoleSpaces(3);
        GeneralFormatter.printOut("RIVELAZIONE EQUILIBRIO DELLA PARTITA", true, false);
        main.consoleSpaces(2);
        b.print();
    }

}
