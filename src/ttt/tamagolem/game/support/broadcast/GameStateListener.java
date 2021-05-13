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

import ttt.tamagolem.game.logic.GameHandler;
import ttt.tamagolem.game.support.Broadcast;

/**
 * Listener dello stato di gioco chiamato per ogni {@link Broadcast#forceBroadcastGameState(ttt.tamagolem.game.logic.GameHandler)
 * } oppure {@link Broadcast#broadcastGameState(java.lang.Integer, java.lang.Object, ttt.tamagolem.game.logic.GameHandler)
 * } solo quando è necessario.
 *
 * @author TTT
 */
public interface GameStateListener {

    /**
     * Viene chiamato all'inizio della partita.
     *
     * @param gh Il gioco corrente.
     */
    public void onStart(GameHandler gh);

    /**
     * Viene chiamato ad ogni inizio turno.
     *
     * @param gh Il gioco corrente.
     */
    public void onNextRound(GameHandler gh);

    /**
     * Viene chiamato appena si conclude una partita.
     *
     * @param gh Il gioco corrente (appena terminato).
     */
    public void onFinish(GameHandler gh);
}
