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
package tamagolem.game.logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Random;
import tamagolem.contents.structure.Player;
import tamagolem.contents.structure.balance.Balance;
import tamagolem.contents.structure.golem.Golem;
import tamagolem.contents.xml.elements.ElementoSecondario;
import tamagolem.contents.xml.elements.Nome;
import tamagolem.game.MainMenu.Difficulty;
import tamagolem.game.support.Broadcast;
import ttt.utils.console.input.ConsoleInput;
import ttt.utils.console.output.GeneralFormatter;

/**
 * Gestisce un gioco.
 *
 * @author TTT
 */
public class GameHandler {

    private final Player p1;
    private final Player p2;
    ArrayList<String> nodes_names;
    Difficulty diff;
    Balance b;

    private boolean finished = false;
    private GameStates current = GameStates.VOID;

    public GameHandler(Player p1, Player p2) {
        this.p1 = p1;
        this.p2 = p2;
        init();
    }

    private void init() {
        diff = (Difficulty) Broadcast.askForGameState(Broadcast.CURRENT_GAME_DIFFICULTY);
        Nome n = (Nome) Broadcast.askForGameState(Broadcast.GAME_NODES);
        nodes_names = new ArrayList<>();
        ArrayList<String> tmp = new ArrayList<>();
        n.getElements().forEach(iel -> {
            ElementoSecondario es = (ElementoSecondario) iel;
            tmp.add(es.getValue());
        });
        Collections.shuffle(tmp);
        Random r = new Random(new Date().getTime());
        int maxNodes = r.nextInt(diff.getMax() - diff.getMin()) + diff.getMin();
        for(int i=0;i<maxNodes;i++){
            nodes_names.add(tmp.get(i));
        }
        b = new Balance(5 * diff.getMin(), nodes_names);
        Broadcast.broadcastGameState(Broadcast.LOSER_PLAYER, null, this);
        Broadcast.broadcastGameState(Broadcast.WINNER_PLAYER, null, this);
        Broadcast.broadcastGameState(Broadcast.CURRENT_GAME_HANDLER, this, this);
    }

    /**
     * Avvia la partita e avverte tutte le istanze in ascolto.
     */
    public void start() {
        //Prima di annunciare l'inizio partita
        current = GameStates.STARTED;
        Broadcast.forceBroadcastGameState(this);
        //Dopo averlo annunciato.
        GeneralFormatter.incrementIndents();
        generateGolem(p1);
        generateGolem(p2);
        GeneralFormatter.decrementIndents();
    }

    private void generateGolem(Player player) {
        if (player != null) {
            Golem g = player.getGolem();
            if (g == null || g.isDead()) {
                current = GameStates.GOLEM_GENERATION;
                if (player.getUsedGolems() <= Broadcast.askForGameValue(Broadcast.MAX_GOLEM_AMOUNT)) {
                    GeneralFormatter.printOut("Genera golem per " + player.getName(), true, false);
                    System.out.println();

                } else {
                    //Questo giocatore ha perso
                    Broadcast.broadcastGameState(Broadcast.LOSER_PLAYER, player, this);
                    Broadcast.broadcastGameState(Broadcast.WINNER_PLAYER, getOpponent(player), this);
                    current = GameStates.FINISHED;
                    Broadcast.forceBroadcastGameState(this);
                }
            }
        }
    }

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
     * Chiede l'abbandono forzato all'utente (prima che la partita si
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
