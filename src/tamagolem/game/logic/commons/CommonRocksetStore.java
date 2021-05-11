/*
 * Copyright 2021 gabri.
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
package tamagolem.game.logic.commons;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;
import java.util.Stack;
import tamagolem.contents.exceptions.UnitializedException;
import tamagolem.contents.graph.Node;
import tamagolem.contents.structure.balance.Balance;
import tamagolem.contents.structure.golem.Rock;
import tamagolem.contents.structure.golem.Rockset;
import tamagolem.game.support.Broadcast;
import ttt.utils.console.menu.utils.Pair;

/**
 *
 * @author gabri
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

    public static CommonRocksetStore getInstance() {
        if (instance == null || instance.isClosed()) {
            instance = new CommonRocksetStore();
        }
        return instance;
    }

    public void setup(Balance b) throws UnitializedException {
        if (setup && b != null) {
            balance = b;
            Integer max_rocks = Broadcast.askForGameValue(Broadcast.COMMON_ROCKS_AVAILABLE);
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

    public boolean preorder(Node type) {
        if (available_rocks.containsKey(type)) {
            Pair<Node, Rock> order = new Pair(type, popRock(type));
            retain.add(order);
        }
        return false;
    }
    
    public void removeOrder(){
        
    }
    
    public Rockset flushOrder(){
        Rock[] rocks = new Rock[retain.size()];
        
    }

    public Set<Node> getAvailableElements() {
        return Collections.unmodifiableSet(available_rocks.keySet());
    }

    public boolean isAvailable(Node type) {
        return available_rocks.containsKey(type);
    }

    public void closeSet() {
        closed = true;
    }

    public boolean isClosed() {
        return closed;
    }
}
