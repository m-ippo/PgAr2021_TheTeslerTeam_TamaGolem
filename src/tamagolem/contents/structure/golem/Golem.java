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
package tamagolem.contents.structure.golem;

import java.util.ArrayList;
import tamagolem.contents.exceptions.UnitializedException;
import tamagolem.contents.structure.events.GolemListener;

/**
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

    public Golem(int life, Rock r1, Rock r2, Rock r3) throws UnitializedException {
        this(life, new Rockset(r1, r2, r3));
    }

    public Golem(int life, Rock... rs) throws UnitializedException {
        this(life, new Rockset(rs));
    }

    public void decrementLifeBy(Integer value) {
        if (value != null && !dead) {
            life -= value;
            if (life <= 0) {
                dead = true;
                dead();
            }
        }
    }

    public Rock getRock() {
        return rset.next();
    }

    public boolean isDead() {
        return dead;
    }

    public void addListener(GolemListener listener) {
        listeners.add(listener);
    }

    public void removeListener(GolemListener listener) {
        listeners.remove(listener);
    }

    private void dead() {
        listeners.forEach(l -> l.onDeath());
    }

}
