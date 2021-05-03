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

/**
 *
 * @author TTT
 */
public class Graph {

    private ArrayList<Link> graph_links = new ArrayList<>();
    private static ArrayList<Node> graph_nodes = new ArrayList<>();

    public Graph() {

    }

    /**
     * Genera la tabella di true-false che determina la direzione degli archi.
     */
    public void generateLinkTable() {

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

}
