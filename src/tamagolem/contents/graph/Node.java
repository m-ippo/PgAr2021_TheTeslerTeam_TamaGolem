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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Rappresenta un nodo.
 *
 * @author TTT
 */
public final class Node implements Comparable<Node> {

    private final String name;
    private final HashMap<Node, Link> links = new HashMap<>();
    private Link cycle;

    protected Node(String name) {
        this.name = name;
        init();
    }

    private void init() {
        cycle = new Link(this, this);
        cycle.lock();
    }

    /**
     * Aggiunge un arco a questo nodo e ne associa il nodo opposto.
     *
     * @param l L'arco da aggiungere.
     */
    public void addLink(Link l) {
        if (!links.containsValue(l)) {
            links.put(l.getLinked(this), l);
        }
    }

    /**
     * Ritorna quanti archi non hanno ancora un valore.
     *
     * @return Il numero di archi non impostati.
     */
    public int getVoidLinks() {
        int to_ret = 0;
        for (Link l : links.values()) {
            if (l.getFrom() == this && !l.isLocked()) {
                to_ret += l.hasValue() ? 0 : 1;
            }
        }
        return to_ret;
    }

    /**
     * Ritorna il primo arco senza valore.
     *
     * @return L'arco se presente, altrimenti {@code null}.
     */
    public Optional<Link> getFirstVoidLink() {
        for (Link l : links.values()) {
            if (!l.hasValue()  && !l.isLocked() && l.getFrom() == this) {
                return Optional.of(l);
            }
        }
        return Optional.empty();
    }

    /**
     * Ritorna la somma (se esiste) dei valori in uscita dal nodo.
     *
     * @return Il valore se maggiore di 0, altrimenti {@code null}.
     */
    public Integer getOutputSum() {
        Integer val = 0;
        for (Link l : links.values()) {
            if (l.hasValue() && l.getFrom() == this) {
                val += l.getPower();
            }
        }
        return val > 0 ? val : null;
    }

    /**
     * Ritorna la somma (se esiste) dei valori in entrata nel nodo.
     *
     * @return Il valore se maggiore di 0, altrimenti {@code null}.
     */
    public Integer getInputSum() {
        Integer val = 0;
        for (Link l : links.values()) {
            if (l.hasValue() && l.getTo() == this) {
                val += l.getPower();
            }
        }
        return val > 0 ? val : null;
    }

    /**
     * Ritorna il nome del nodo (detto anche elemento).
     *
     * @return Il nome.
     */
    public String getName() {
        return name;
    }

    /**
     * Ritorna gli archi che escono dal nodo.
     *
     * @return Lista di archi in uscita.
     */
    public List<Link> getOutputLinks() {
        ArrayList<Link> to_ret = new ArrayList<>();
//        links.values().stream().filter((l) -> {
//            return l.getFrom() == this; 
//        }).forEach(l -> to_ret.add(l));
        for (Link l : links.values()) {
            if (l.getFrom() == this) {
                to_ret.add(l);
            }
        }
        return Collections.unmodifiableList(to_ret);
    }

    /**
     * Ritorna gli archi che entrano nel nodo.
     *
     * @return Lista di archi in ingresso.
     */
    public List<Link> getInputLinks() {
        ArrayList<Link> to_ret = new ArrayList<>();
        for (Link l : links.values()) {
            if (l.getTo() == this) {
                to_ret.add(l);
            }
        }
        return Collections.unmodifiableList(to_ret);
    }

    /**
     * Ritorna il numero di archi in entrata senza valore.
     *
     * @return Il numero di archi.
     */
    public long getVoidInputLinksCount() {
        return getInputLinks().stream().filter(link -> {
            return !link.hasValue();
        }).count();
    }

    /**
     * Ritorna il numero di archi in uscita senza valore.
     *
     * @return Il numero di archi.
     */
    public long getVoidOutputLinksCount() {
        return getOutputLinks().stream().filter(link -> {
            return !link.hasValue();
        }).count();
    }

    /**
     * Ritorna gli archi in entrata senza valore.
     *
     * @return Il numero di archi.
     */
    public List<Link> getVoidInputLinks() {
        ArrayList<Link> to_ret = new ArrayList<>();
        getInputLinks().stream().filter(link -> {
            return !link.hasValue();
        }).forEach((t) -> {
            to_ret.add(t);
        });
        return Collections.unmodifiableList(to_ret);
    }

    /**
     * Ritorna gli archi in uscita senza valore.
     *
     * @return Il numero di archi.
     */
    public List<Link> getVoidOutputLinks() {
        ArrayList<Link> to_ret = new ArrayList<>();
        getOutputLinks().stream().filter(link -> {
            return !link.hasValue();
        }).forEach((t) -> {
            to_ret.add(t);
        });
        return Collections.unmodifiableList(to_ret);
    }

    /**
     * Ritorna la lista chiave-valore di nodi e archi.
     *
     * @return Map non modificabile.
     */
    public Map<Node, Link> getToNodes() {
        return Collections.unmodifiableMap(links);
    }

    /**
     * Restituisce l'arco per arrivare ad un altro nodo. Se il nodo d'arrivo è
     * uguale al nodo di partenza viene ritornato l'arco che rappresenta il
     * ciclo su se stesso.
     *
     * @param n Il nodo di arrivo.
     * @return L'arco che rappresenta il percorso da compiere.
     */
    public Link to(Node n) {
        return n == this ? cycle : links.get(n);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Entrano in \"").append(name).append("\":");
        getInputLinks().forEach(l -> sb.append("\n\t-").append(l.getLinked(this).getName()));
        sb.append("\nEscono da \"").append(name).append("\":");
        getOutputLinks().forEach(l -> sb.append("\n\t-").append(l.getLinked(this).getName()));
        return sb.toString(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int compareTo(Node o) {
        int val1 = this.getOutputLinks().size();
        int val2 = o.getOutputLinks().size();
        if (val1 > val2) {
            return -1;
        } else if (val1 < val2) {
            return 1;
        }
        return 0;
    }

}
