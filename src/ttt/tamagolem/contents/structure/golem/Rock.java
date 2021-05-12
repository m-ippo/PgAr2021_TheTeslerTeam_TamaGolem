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

import java.util.Objects;
import ttt.tamagolem.contents.graph.Link;
import ttt.tamagolem.contents.graph.Node;

/**
 * Rappresenta una pietra.
 *
 * @author TTT
 */
public class Rock {

    public Node type;

    public Rock(Node type) {
        this.type = type;
    }

    /**
     * Ritorna il valore dell'arco associato all'elemento di questa pietra
     * contro un'altra pietra.
     *
     * @param r La pietra contro con cui andare.
     * @return Il valore d'interazione se esiste il link, nel caso questa pietra
     * perda viene ritornato il valore d'interazione negativo. Nel caso non
     * esista un'interazione viene ritornato 0.
     */
    public int against(Rock r) {
        Link l = type.to(r.type);
        Integer p = l != null ? l.getPower() : null;
        return l != null && p != null ? (l.getFrom() == type ? p : -l.getPower()) : 0;
    }

    /**
     * Controlla se una pietra vince contro un'altra.
     *
     * @param r La pietra di controllare.
     * @return {@code true} nel caso la pietra corrente vinca.
     */
    public boolean winsAgainst(Rock r) {
        if (r != null) {
            Link l = type.to(r.type);
            return l != null ? l.getFrom() == type : false;
        }
        return false;
    }

    /**
     * Ritorna il nome dell'elementare.
     *
     * @return Il nome dell'elementare.
     */
    public String getTypeName() {
        return type.getName();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Rock) {
            Rock r = (Rock) obj;
            return r.type == type;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + Objects.hashCode(this.type);
        return hash;
    }

}
