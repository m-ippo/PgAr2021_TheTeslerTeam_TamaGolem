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
package tamagolem.game.support;

import java.util.ArrayList;
import java.util.HashMap;
import tamagolem.game.support.broadcast.GameValueListener;

/**
 *
 * @author TTT
 */
public class Broadcast {

    private final static HashMap<String, Integer> game_values = new HashMap<>();

    /*
                        COSTANTI
     */
    public final static String MAX_GOLEM_LIFE = "g_life";
    public final static String MAX_GOLEM_AMOUNT = "g_amn";
    public final static String COMMON_ROCKS_AVAILABLE = "cmmn_rks";
    public final static String MAX_ROCKS_PER_ELEMENT = "elms_rks";
    public final static String MAX_ROCKS_PER_GOLEM = "g_rks";

    private static final ArrayList<GameValueListener> value_listeners = new ArrayList<>();

    public static void broadcastGameValue(String key, Integer value) {
        game_values.put(key, value);
        value_listeners.forEach(l -> l.onUpdate(key));
    }

    public static Integer askForGameValue(String key) {
        return game_values.get(key);
    }

    public static void addGameValueListener(GameValueListener listener) {
        value_listeners.add(listener);
    }

    public static void removeGameValueListener(GameValueListener listener) {
        value_listeners.remove(listener);
    }

}
