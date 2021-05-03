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
package tamagolem.contents.graph;

/**
 *
 * @author TTT
 */
public final class Link {

    private Integer power = null;
    private final Node from;
    private final Node to;

    public Link(Node from, Node to) {
        this.from = from;
        this.to = to;
    }

    /**
     * Restituisce il nodo da cui l'arco parte.
     *
     * @return Il nodo associato.
     */
    public Node getFrom() {
        return from;
    }

    /**
     * Restituisce il nodo a cui l'arco arriva.
     *
     * @return Il nodo associato.
     */
    public Node getTo() {
        return to;
    }

    /**
     * Ritorna la Potenza d'Interazione associata a quest'arco.
     *
     * @return Il valore associato se è stato calcolato precedentemente,
     * altrimenti {@code null}.
     */
    public Integer getPower() {
        return power;
    }

    /**
     * Imposta il valore della Potenza d'Interazione tra i due nodi.
     *
     * @param power Il valore della potenza.
     */
    public void setPower(Integer power) {
        this.power = power;
    }

    /**
     * Controlla se l'arco è già stato inizializzato.
     *
     * @return Ritorna {@code true} se l'arco è già stato calcolato.
     */
    public boolean hasValue() {
        return power != null && power > 0;
    }

    /**
     * Ritorna il nodo opposto.
     *
     * @param n Il nodo appartenente a questo arco.
     * @return Il nodo opposto a quello passato come parametro, altrimenti
     * {@code null} se il nodo passato non fa parte dell'arco.
     */
    public Node getLinked(Node n) {
        if (n != null && (n == from || n == to)) {
            return n == from ? to : from;
        }
        return null;
    }

}
