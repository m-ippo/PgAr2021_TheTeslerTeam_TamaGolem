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
import java.util.List;
import java.util.Random;

/**
 *
 * @author TTT
 */
public class Graph {

    private final ArrayList<Link> graph_links = new ArrayList<>();
    private static ArrayList<Node> graph_nodes = new ArrayList<>();

    private final Random rnd = new Random();

    public Graph() {

    }

    public void print() {
        graph_links.forEach(l -> System.out.println(l.toString()));
    }

    /**
     * Genera la tabella di true-false che determina la direzione degli archi e
     * succesivamente li genera.
     */
    public void generateLinkTable() {
        graph_links.clear();
        Collections.shuffle(graph_nodes, rnd);
        for (int i = 0; i < graph_nodes.size() - 1; i++) {
            Node main = graph_nodes.get(i);
            for (int y = i + 1; y < graph_nodes.size(); y++) {
                Node secondary = graph_nodes.get(y);
                boolean rnd_bool = rnd.nextBoolean();
                if (main.getInputLinks().size() == (graph_nodes.size() - 2) && main.getOutputLinks().isEmpty()) {
                    rnd_bool = true;
                } else if (main.getOutputLinks().size() == (graph_nodes.size() - 2) && main.getInputLinks().isEmpty()) {
                    rnd_bool = false;
                }
                Link l;
                if (rnd_bool) {
                    l = generateLink(main, secondary);
                } else {
                    l = generateLink(secondary, main);
                }
                graph_links.add(l);
            }
        }
    }

    /**
     * Genera un nuovo arco tra due nodi.
     *
     * @param from Il nodo da cui parte (vincitore).
     * @param to Il nodo a cui arriva (perdente).
     * @return Il link appena generato.
     */
    public static Link generateLink(Node from, Node to) {
        Link l = new Link(from, to);
        from.addLink(l);
        to.addLink(l);
        return l;
    }

    /**
     * Genera una lista di nodi avendo una lista dei nomi e li imposta per il
     * nuovo grafo.
     *
     * @param names Lista di nomi degli elementi.
     * @return Lista di nodi.
     */
    public static List<Node> generateNodes(ArrayList<String> names) {
        graph_nodes = new ArrayList<>();
        names.stream().forEach(name -> {
            Node n = new Node(name);
            graph_nodes.add(n);
        });
        return Collections.unmodifiableList(graph_nodes);
    }

    /**
     * Ritorna una lista non modificabile di nodi comuni ai grafi.
     *
     * @return Lista di nodi.
     */
    public List<Node> getNodes() {
        return Collections.unmodifiableList(graph_nodes);
    }

}
