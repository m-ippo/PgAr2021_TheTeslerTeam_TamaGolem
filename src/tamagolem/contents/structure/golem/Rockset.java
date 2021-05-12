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

import java.util.Arrays;
import tamagolem.contents.exceptions.UnitializedException;
import tamagolem.game.support.Broadcast;

/**
 * Rappresenta l'insieme di pietre disponibili per un golem ed offre i metodi
 * minimi per la rotazione delle pietre.
 *
 * @author TTT
 */
public final class Rockset {

    private final Rock[] rocks;
    private int current = 0;
    private int filler = 0;

    public Rockset() throws UnitializedException {
        Integer mx = Broadcast.askForGameValue(Broadcast.MAX_ROCKS_PER_GOLEM);
        if (mx != null) {
            this.rocks = new Rock[mx];
        } else {
            throw new UnitializedException("Il sistema non ha ancora generato i valori.");
        }
    }

    public Rockset(Rock... rocks) throws UnitializedException {
        this();
        for (Rock r : rocks) {
            pushRock(r);
        }
    }

    /**
     * Inserisce una nuova pietra al set.
     *
     * @param r La pietra da aggiungere.
     */
    public void pushRock(Rock r) {
        if (filler < rocks.length && r != null) {
            rocks[filler] = r;
            filler++;
        }
    }

    /**
     * Ritorna la pietra successiva e passa alla prossima pietra.
     *
     * @return La pietra successiva.
     */
    public Rock next() {
        if (current > rocks.length - 1) {
            current = 0;
        }
        return rocks[current++];
    }

    /**
     * Ritorna il set di rocce.
     *
     * @return L'array ordinato secondo i valori iniziali e non secondo il
     * corrente ordine.
     */
    public Rock[] getRockset() {
        return rocks;
    }

    /**
     * Ritorna alla pietra precedente.
     *
     * @return La pietra precedente
     */
    public Rock previous() {
        if (current < 0) {
            current = rocks.length - 1;
        }
        return rocks[current--];
    }

    /**
     * Sposta la rotazione di "I" passi.
     *
     * @param i I passi da eseguire.
     */
    public void addOffset(Integer i) {
        if (i != null && i > 0 && (current + i) < rocks.length) {
            current += i;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Rockset) {
            Rockset r = (Rockset) obj;
            if (r.rocks.length == rocks.length) {
                for (Rock rock : rocks) {
                    if (!rock.equals(r.next())) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + Arrays.deepHashCode(this.rocks);
        return hash;
    }

}
