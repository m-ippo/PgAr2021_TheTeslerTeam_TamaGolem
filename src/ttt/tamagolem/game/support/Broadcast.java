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
package ttt.tamagolem.game.support;

import java.util.ArrayList;
import java.util.HashMap;
import ttt.tamagolem.game.logic.GameHandler;
import ttt.tamagolem.game.support.broadcast.GameStateListener;
import ttt.tamagolem.game.support.broadcast.GameValueListener;

/**
 * Classe che mette a disposizione dell'intero progetto i valori e gli stati di
 * una partita in gioco.
 *
 * @author TTT
 */
public class Broadcast {

    //<editor-fold defaultstate="collapsed" desc="Valodi di gioco dinamici">
    /*
                VALORI DI GIOCO DINAMICI
     */
 /*
                        Costanti
     */
    /**
     * Chiave del valore per la vita massima possibile per un golem.
     */
    public final static Integer MAX_GOLEM_LIFE = 0;//"g_life";
    /**
     * Chiave del valore per il massimo numero di golem che un giocatore può
     * giocare.
     */
    public final static Integer MAX_GOLEM_AMOUNT = 1;//"g_amn";
    /**
     * Chiave del valore di pietre elementari disponibile nella scorta comune.
     */
    public final static Integer COMMON_ROCKS_AVAILABLE = 2;//"cmmn_rks";
    /**
     * Chiave del valore che rappresenta il massimo numero di pietre elementari
     * per ogni elemento (nodo).
     */
    public final static Integer MAX_ROCKS_PER_ELEMENT = 3;//"elms_rks";
    /**
     * Chiave del valore che indica il massimo numero di pietre per i golem
     * schierati.
     */
    public final static Integer MAX_ROCKS_PER_GOLEM = 4;//"g_rks";

    private final static HashMap<Integer, Integer> game_values = new HashMap<>();
    private static final ArrayList<GameValueListener> value_listeners = new ArrayList<>();

    /**
     * Annuncia a tutti gli ascoltatori il nuovo valore assegnato alla chiave
     *
     * @param key La chiave da aggiornare.
     * @param value Il nuovo valore.
     */
    public static void broadcastGameValue(Integer key, Integer value) {
        game_values.put(key, value);
        value_listeners.forEach(l -> l.onUpdate(key));
    }

    /**
     * Richiede il valore associato ad una chiave.
     *
     * @param key La chiave da cercare.
     * @return Il valore se presente, altrimenti {@code null}.
     */
    public static Integer askForGameValue(Integer key) {
        return game_values.get(key);
    }

    /**
     * Aggiunge un nuovo ascoltatore per il broadcast dei valori.
     *
     * @param listener Il listener da aggiungere.
     */
    public static void addGameValueListener(GameValueListener listener) {
        value_listeners.add(listener);
    }

    /**
     * Rimuove dall'ascolto del broadcast un listener.
     *
     * @param listener Il listener da rimuovere.
     */
    public static void removeGameValueListener(GameValueListener listener) {
        value_listeners.remove(listener);
    }

//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Stato della partita">
    /**
     * Chiave del valore associata al giocatore vincitore.
     */
    public final static Integer WINNER_PLAYER = 0;//"winner";
    /**
     * Chiave del valore associata al giocatore perdente.
     */
    public final static Integer LOSER_PLAYER = 1;//"loser";
    /**
     * Chiave del valore del gioco corrente.
     */
    public final static Integer CURRENT_GAME_HANDLER = 2;//"gm";
    /**
     * Chiave del valore della difficoltà del gioco corrente.
     */
    public final static Integer CURRENT_GAME_DIFFICULTY = 3;
    /**
     * Chiave del valore della lista di nodi.
     */
    public final static Integer GAME_NODES = 4;
    /**
     * Chiave del valore dell'equilibrio della partita.
     */
    public final static Integer GAME_BALANCE = 5;

    private final static HashMap<Integer, Object> game_statuses = new HashMap<>();
    private static final ArrayList<GameStateListener> state_listeners = new ArrayList<>();

    /**
     * Annuncia un nuovo valore della partita in base allo stato del gioco.
     *
     * @param key La chiave del valore da aggiornare.
     * @param value Il nuovo valore.
     * @param gm La partita corrente.
     */
    public static void broadcastGameState(Integer key, Object value, GameHandler gm) {
        game_statuses.put(key, value);
        if (gm != null) {
            switch (gm.getCurrentState()) {
                case STARTED:
                    state_listeners.forEach(l -> l.onStart(gm));
                    break;
                case NEXT_ROUND:
                    state_listeners.forEach(l -> l.onNextRound(gm));
                    break;
                case FINISHED:
                    state_listeners.forEach(l -> l.onFinish(gm));
                    break;
            }
        }
    }

    /**
     * Forza un annuncio sullo stato della partita corrente.
     *
     * @param gm La partita corrente.
     */
    public static void forceBroadcastGameState(GameHandler gm) {
        if (gm != null) {
            switch (gm.getCurrentState()) {
                case STARTED:
                    state_listeners.forEach(l -> l.onStart(gm));
                    break;
                case NEXT_ROUND:
                    state_listeners.forEach(l -> l.onNextRound(gm));
                    break;
                case FINISHED:
                    state_listeners.forEach(l -> l.onFinish(gm));
                    break;
            }
        }
    }

    /**
     * Richiede un valore associato allo stato della partita.
     *
     * @param key La chiave associata al valore.
     * @return Il valore se presente, altrimenti {@code null}.
     */
    public static Object askForGameState(Integer key) {
        return game_statuses.get(key);
    }

    /**
     * Aggiunge un nuovo ascoltatore per il broadcast degli stati.
     *
     * @param listener Il listener da aggiungere.
     */
    public static void addGameStateListener(GameStateListener listener) {
        state_listeners.add(listener);
    }

    /**
     * Rimuove dall'ascolto del broadcast un listener.
     *
     * @param listener Il listener da rimuovere.
     */
    public static void removeGameStateListener(GameStateListener listener) {
        state_listeners.remove(listener);
    }

//</editor-fold>
    public static void resetAll() {
        game_values.clear();
        game_statuses.clear();
    }
}
