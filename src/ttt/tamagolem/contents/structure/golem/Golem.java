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
package ttt.tamagolem.contents.structure.golem;

import java.util.ArrayList;
import ttt.tamagolem.contents.exceptions.UnitializedException;
import ttt.tamagolem.contents.structure.events.GolemListener;

/**
 * Crea un nuovo golem.
 *
 * @author TTT
 */
public class Golem {

    private int life;
    private final Rockset rset;
    private final ArrayList<GolemListener> listeners = new ArrayList<>();
    private boolean dead = false;

    public Golem(int life, Rockset r) {
        this.life = life;
        this.rset = r;
    }

    public Golem(int life, Rock... rs) throws UnitializedException {
        this(life, new Rockset(rs));
    }

    /**
     * Decrementa la vita del golem di un certo valore.
     *
     * @param value Quantità da togliere, nel caso il valore sia negativo verrà
     * cambiato di segno.
     */
    public void decrementLifeBy(Integer value) {
        if (value != null && !dead) {
            life -= value < 0 ? -value : value;
            if (life <= 0) {
                dead = true;
                dead();
            }
        }
    }

    /**
     * Ritorna la prossima pietra nel set del golem.
     *
     * @return La prossima pietra.
     */
    public Rock getRock() {
        return rset.next();
    }

    /**
     * Controlla se il golem è morto.
     *
     * @return {@code true} nel caso sia morto.
     */
    public boolean isDead() {
        return dead;
    }

    /**
     * Aggiungi un listener al golem.
     *
     * @param listener Il listener da mettere in ascolto.
     */
    public void addListener(GolemListener listener) {
        listeners.add(listener);
    }

    /**
     * Rimuovi listener dal golem.
     *
     * @param listener Il listener in ascolto.
     */
    public void removeListener(GolemListener listener) {
        listeners.remove(listener);
    }

    private void dead() {
        listeners.forEach(l -> l.onDeath());
        listeners.clear();
    }

    /**
     * Ritorna il set di pietre di questo golem. Usare con molta cautela.
     *
     * @return Il Rockset associato.
     */
    public Rockset getRockset() {
        return rset;
    }

}
