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
package ttt.tamagolem.game.logic.commons;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Optional;
import java.util.Set;
import java.util.Stack;
import ttt.tamagolem.contents.exceptions.UnitializedException;
import ttt.tamagolem.contents.graph.Node;
import ttt.tamagolem.contents.structure.balance.Balance;
import ttt.tamagolem.contents.structure.golem.Rock;
import ttt.tamagolem.contents.structure.golem.Rockset;
import ttt.tamagolem.game.support.Broadcast;
import ttt.utils.console.menu.utils.Pair;

/**
 * Rappresenta la scorta comune di pietre disponibili.
 *
 * @author TTT
 */
public class CommonRocksetStore {

    HashMap<Node, Stack<Rock>> available_rocks = new HashMap<>();

    private static CommonRocksetStore instance;
    private boolean setup = true;
    private boolean closed = false;
    private Balance balance = null;
    ArrayList<Pair<Node, Rock>> retain = new ArrayList<>();

    private CommonRocksetStore() {

    }

    /**
     * Ritorna l'unica istanza creata per il gioco. Nel caso si voglia creare
     * una nuova istanza bisogna chiudere la precedente chiamando il metodo {@link #closeSet()
     * }.
     *
     * @return L'instanza della scorta comune del gioco corrente.
     */
    public static CommonRocksetStore getInstance() {
        if (instance == null || instance.isClosed()) {
            instance = new CommonRocksetStore();
        }
        return instance;
    }

    /**
     * Inizializza la scorta comune di pietre presenti per la partita corrente.
     *
     * @param b L'equilibrio del sistema da seguire per la generazione.
     * @throws UnitializedException Viene lanciata nel caso in cui il gioco non
     * è ancora stato avviato.
     */
    public void setup(Balance b) throws UnitializedException {
        if (setup && b != null) {
            balance = b;
            Integer max_rocks = Broadcast.askForGameValue(Broadcast.MAX_ROCKS_PER_ELEMENT);
            if (max_rocks != null) {
                available_rocks.clear();
                balance.getNodes().forEach(c_r -> {
                    Stack<Rock> rocks = new Stack<>();
                    for (int i = 0; i < max_rocks; i++) {
                        rocks.push(new Rock(c_r));
                    }
                    available_rocks.put(c_r, rocks);
                });
                setup = false;
            } else {
                throw new UnitializedException("Il gioco non è ancora iniziato. Impossibile richiedere i valori.");
            }
        }
    }

    /**
     * Rimuove una pietra dalla scorta comune e la ritorna come risultato.
     *
     * @param type Il tipo di pietra da rimuovere
     * @return La pietra rimossa se possibile, altrimenti {@code null}.
     */
    public Rock popRock(Node type) {
        if (type != null) {
            Stack<Rock> to_get = available_rocks.get(type);
            if (to_get != null && !to_get.empty()) {
                Rock to_ret = to_get.pop();
                if (to_get.isEmpty()) {
                    available_rocks.remove(type);
                }
                return to_ret;
            }
        }
        return null;
    }

    /**
     * Piazza un ordine su una pietra di un certo tipo (nodo)
     *
     * @param type Il nodo secondo cui cercare la pietra da ordinare.
     * @return {@code true} nel caso in cui la pietra sia stata preordinata con
     * successo, altrimenti in caso contrario viene ritornato {@code false}.
     */
    public boolean preorder(Node type) {
        if (available_rocks.containsKey(type)) {
            Pair<Node, Rock> order = new Pair(type, popRock(type));
            retain.add(order);
            return true;
        }
        return false;
    }

    /**
     * Rimuovi un ordine precedente fatto dalla lista. La pietra ordinata verrà
     * successivamente ritornata alla scorta comune. L'ordine rimosso è la prima
     * pietra che aggrega il nodo cercato.
     *
     * @param type Il nodo la cui pietra aggrega.
     */
    public void removePreorder(Node type) {
        Pair<Node, Rock> p = null;
        Optional<Pair<Node, Rock>> findFirst = retain.stream().filter(t -> {
            return t.getKey() == type;
        }).findFirst();
        if (findFirst.isPresent()) {
            p = findFirst.get();
            retain.remove(p);
            available_rocks.get(type).push(p.getValue());
        }
    }

    /**
     * Data una serie di ordini precedenti viene creato un {@link Rockset}
     * contenente le pietre ordinate, le pietre in questo modo sono
     * definitivamente tolte dalla scorta.
     *
     * @return Il {@link Rockset} appena creato.
     * @throws UnitializedException Nel caso le variabili di gioco non siano
     * ancora state inizializzate.
     */
    public Rockset flushOrder() throws UnitializedException {
        Rock[] rocks = new Rock[retain.size()];
        int cnt = 0;
        for (Pair<Node, Rock> pr : retain) {
            rocks[cnt] = pr.getValue();
            cnt++;
        }
        Rockset rs = new Rockset(rocks);
        retain.clear();
        return rs;
    }

    /**
     * Resituisce il numero di pietre selezionate.
     *
     * @return La quantità di pietre preordinate.
     */
    public int getOrderSize() {
        return retain.size();
    }

    /**
     * Restituisce il conteggio di quante pietre rimangono per un certo
     * elemento.
     *
     * @param n Il nodo da controllare.
     * @return Il conteggio.
     */
    public int getRocksLeftCount(Node n) {
        if (n != null) {
            if (available_rocks.containsKey(n)) {
                return available_rocks.get(n).size();
            }
        }
        return 0;
    }

    /**
     * Ritorna la lista di nodi che hanno almeno una pietra rimanente nella
     * scorta comune.
     *
     * @return Il set contenente una lista di {@link Node}..
     */
    public Set<Node> getAvailableElements() {
        return Collections.unmodifiableSet(available_rocks.keySet());
    }

    /**
     * Controlla se per un certo nodo/elemento sono presenti rocce.
     *
     * @param type L'elemento secondo cui cercare.
     * @return {@code true} se esiste almeno una pietra rimanente per il nodo
     * cercato.
     */
    public boolean isAvailable(Node type) {
        return available_rocks.containsKey(type);
    }

    /**
     * Chiude il set corrente.
     */
    public void closeSet() {
        closed = true;
    }

    /**
     * Controlla se questo set comune è stato chiuso.
     *
     * @return {@code true} solamente se il set è chiuso.
     */
    public boolean isClosed() {
        return closed;
    }
}
