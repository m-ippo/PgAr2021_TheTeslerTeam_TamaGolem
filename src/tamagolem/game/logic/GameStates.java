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

/**
 * Stati possibili di una partita.
 *
 * @author TTT
 */
public enum GameStates {
    /**
     * Partita non ancora iniziata.
     */
    VOID,
    /**
     * Generazione di un golem (gioco in pausa).
     */
    GOLEM_GENERATION,
    /**
     * Partita iniziata.
     */
    STARTED,
    /**
     * Round iniziato.
     */
    NEXT_ROUND,
    /**
     * Partita conclusa.
     */
    FINISHED
}
