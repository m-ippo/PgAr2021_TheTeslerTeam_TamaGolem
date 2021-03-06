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
package ttt.tamagolem.game.support.broadcast;

import ttt.tamagolem.game.support.Broadcast;

/**
 * Listener dei valori in {@link Broadcast}.
 *
 * @author TTT
 */
public interface GameValueListener {

    /**
     * Viene chiamato ogni volta che nella classe {@link Broadcast} viene
     * aggiornato un valore di gioco.
     *
     * @param key La chiave che corrisponde al valore aggiornato.
     */
    public void onUpdate(Integer key);
}
