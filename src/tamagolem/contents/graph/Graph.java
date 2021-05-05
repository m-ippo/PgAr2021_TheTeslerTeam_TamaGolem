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
import java.util.Optional;
import java.util.Random;
import tamagolem.contents.exceptions.UnitializedException;
import tamagolem.contents.graph.comparators.NodeChainedComparator;
import tamagolem.contents.graph.comparators.NodeOutputsCMP;
import tamagolem.contents.graph.comparators.NodeVoidOutputLinkCMP;

/**
 * Rappresenta un grafo.
 *
 * @author TTT
 */
public class Graph {

    private final ArrayList<Link> graph_links = new ArrayList<>();
    private static ArrayList<Node> graph_nodes;

    private final int max_power;

    private final Random rnd = new Random();

    public Graph(int MaxPower, ArrayList<String> names) {
        generateNodes(names);
        if (MaxPower > (graph_nodes.size() - 1)) {
            max_power = MaxPower;
        } else {
            throw new IllegalArgumentException("La potenza massima minima è troppo minima.");
        }
    }

    /**
     * Stampa relazioni tra i nodi.
     */
    public void print() {
        graph_links.forEach(l -> System.out.println(l.toString()));
    }

    /**
     * Genera la tabella di true-false che determina la direzione degli archi e
     * succesivamente li genera.
     *
     * @throws tamagolem.contents.exceptions.UnitializedException Nel caso in
     * cui i nodi non siano ancora stati inizializzati.
     */
    public void generateLinkTable() throws UnitializedException {
        if (graph_nodes != null) {
            graph_links.clear();
            Collections.shuffle(graph_nodes, rnd);
            for (int i = 0; i < graph_nodes.size() - 1; i++) {
                Node main = graph_nodes.get(i);
                for (int y = i + 1; y < graph_nodes.size(); y++) {
                    Node secondary = graph_nodes.get(y);
                    boolean direzione = rnd.nextBoolean();
                    if ((main.getInputLinks().size() == (graph_nodes.size() - 2) && main.getOutputLinks().isEmpty())
                            || (secondary.getOutputLinks().size() == (graph_nodes.size() - 2) && secondary.getInputLinks().isEmpty())) {
                        direzione = true;
                    } else if ((main.getOutputLinks().size() == (graph_nodes.size() - 2) && main.getInputLinks().isEmpty())
                            || (secondary.getInputLinks().size() == (graph_nodes.size() - 2) && secondary.getOutputLinks().isEmpty())) {
                        direzione = false;
                    }
                    Link l;
                    if (direzione) {
                        l = generateLink(main, secondary);
                    } else {
                        l = generateLink(secondary, main);
                    }
                    graph_links.add(l);
                }
            }
            Collections.sort(graph_nodes);
            
            
            
        } else {
            throw new UnitializedException("I nodi non sono ancora stati inizializzati. Impossibile generare gli archi.");
        }
    }

    /**
     * Ritorna il nodo e l'arco associato che secondo la logica prestabilita
     * necessita di essere completato.<p>
     * L'ordine è secondo
     *
     * @param previous
     * @return L'Arco da completare secondo l'ordine. Ritorna {@code null} solo
     * quando non esistono più archi da completare.
     */
    public Link getAttractionLink(Link previous) {
        ArrayList<Node> to_be_ordered = new ArrayList<>();
        graph_nodes.stream().filter((t) -> {
            return t.getVoidLinks() > 0;
        }).forEach((t) -> {
            to_be_ordered.add(t);
        });
        if (previous != null && previous.getFrom().getVoidOutputLinksCount() > 0) {
            for (Node n : to_be_ordered) {
                if (n.getVoidInputLinksCount() == 1 || n.getVoidOutputLinksCount() == 1) {
                    return n.getFirstVoidLink().get();
                }
            }
            List<Link> archi_da_nodo_from_con_piu_output_vuoti = previous.getFrom().getVoidOutputLinks();
            ArrayList<Node> nodi_connessi_a_nodo_from = new ArrayList<>();
            archi_da_nodo_from_con_piu_output_vuoti.forEach(l -> nodi_connessi_a_nodo_from.add(l.getTo()));

            ArrayList<Node> nodi_connessi_a_nodo_from_incompleti = new ArrayList<>();
            nodi_connessi_a_nodo_from.stream().filter((t) -> {
                return t.getVoidOutputLinksCount() > 0;
            }).forEach(nodo_con_archi_incompleti -> nodi_connessi_a_nodo_from_incompleti.add(nodo_con_archi_incompleti));
            Collections.sort(nodi_connessi_a_nodo_from_incompleti, new NodeVoidOutputLinkCMP());
            return nodi_connessi_a_nodo_from_incompleti.get(0).getFirstVoidLink().get();
        }
        NodeChainedComparator ncc = new NodeChainedComparator(/*new NodeVoidOutputLinkCMP(),*/new NodeOutputsCMP());
        Collections.sort(to_be_ordered, ncc);
        Optional<Node> findFirst = to_be_ordered.stream().findFirst();
        return findFirst.isPresent() ? findFirst.get().getFirstVoidLink().orElse(null) : null;
    }

    /**
     * Genera un nuovo arco tra due nodi.
     *
     * @param from Il nodo da cui parte (vincitore).
     * @param to Il nodo a cui arriva (perdente).
     * @return Il link appena generato.
     */
    private static Link generateLink(Node from, Node to) {
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
    private static List<Node> generateNodes(ArrayList<String> names) {
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

    public void generateLinkValues() {
        Link attuale = getAttractionLink(null);
        while (attuale != null) {
            Node from = attuale.getFrom();
            Node to = attuale.getTo();
            if (from.getInputSum() == null) {//Nodo from non ha delle entrate
                if (to.getVoidInputLinksCount() == 1 && to.getVoidOutputLinksCount() == 0) {//Se il nodo TO è completo (tranne quest'ultimo arco), allora calcolo per differenza
                    Integer val = to.getInputSum();
                    attuale.setPower(to.getOutputSum() - (val == null ? 0 : val));
                } else if (to.getVoidOutputLinksCount() == 0 && to.getVoidInputLinksCount() > 1) {//se il nodo To è completo nelle uscite, ma non nelle entrate
                    //[1 % (Somma uscite nodo To - Somma Entrate nodo To -(n°entrate To -1))]
                    Integer in = to.getInputSum();
                    int val = to.getOutputSum() - (in == null ? 0 : in) - ((int) to.getVoidInputLinksCount() - 1);
                    if (val <= 0) {
                        System.out.println("negat");
                    }
                    attuale.setPower(rnd.nextInt(val) + 1);
                } else {//caso generale
                    //[1 % M]
                    //int min = Math.max((to.getVoidInputLinksCount() == 1 ? (int) to.getVoidOutputLinksCount() : 1), (from.getVoidOutputLinksCount() == 1 ? (int) to.getVoidInputLinksCount() : 1));
                    //attuale.setPower(rnd.nextInt(max_power) + min);
                    attuale.setPower(rnd.nextInt(max_power) + 1);
                }
            } else {//Nodo from ha entrate.
                if ((from.getVoidOutputLinksCount() == 1 && from.getVoidInputLinksCount() == 0) || (to.getVoidInputLinksCount() == 1 && to.getVoidOutputLinksCount() == 0)) {//Se FROM ha una solo uscita o TO ha una sola entrata 
                    //calcolo per differenza
                    if (from.getVoidOutputLinksCount() == 1) {//Se FROM ha una sola uscita
                        Integer val = from.getOutputSum();
                        attuale.setPower(from.getInputSum() - (val == null ? 0 : val));
                    } else {
                        Integer val = to.getInputSum();
                        attuale.setPower(to.getOutputSum() - (val == null ? 0 : val));
                    }
                } else if (from.getVoidInputLinksCount() == 0) {//Nodo FROM ha TUTTE le entrate complete | si esclude il caso in cui è rimasta l'ultima uscita
                    //[1 %  (Somma valori entrate FROM - somma valori uscite FROM - (n°uscite rimanenti FROM -1))]
                    Integer out = from.getOutputSum();
                    /*int max_from = from.getInputSum() - (out == null ? 0 : out) - ((int) from.getVoidOutputLinksCount() - 1);
                    int min_to = (int) to.getVoidOutputLinksCount();
                    if (max_from <= 0) {
                        System.out.println("negat");
                    }
                    int val = (to.getVoidInputLinksCount() == 1 ? rnd.nextInt(max_from - min_to) + Math.max(min_to, 1) : rnd.nextInt(max_from) + 1);
                    attuale.setPower(val);*/
                    int val = from.getInputSum() - (out == null ? 0 : out) - ((int) from.getVoidOutputLinksCount() - 1);
                    if (val <= 0) {
                        System.out.println("negat");
                    }
                    attuale.setPower(rnd.nextInt(val) + 1);
                } /*else if () {//Nodo TO non ha valori in uscita
                    //caso generale
                    //[1 % M]
                }*/ else if (to.getVoidOutputLinksCount() == 0) {//Nodo TO ha TUTTE le uscite complete
                    //[1 % (Somma valori uscite TO - somma valori entrare TO - (n°entrate rimanenti TO - 1))]
                    Integer in = to.getInputSum();
                    int val = to.getOutputSum() - (in == null ? 0 : in) - ((int) to.getVoidInputLinksCount() - 1);
                    if (val <= 0) {
                        System.out.println("negat");
                    }
                    attuale.setPower(rnd.nextInt(val) + 1);
                } else {//caso generale
                    //[1 % M]
                    attuale.setPower(rnd.nextInt(max_power) + 1);
                }
            }
            if (attuale.getPower() != null) {
                attuale.lock();
            }
            System.out.println("GENERATO : " + attuale);
            attuale = getAttractionLink(attuale);
        }
    }

    public ArrayList<Link> getGraph_links() {
        return graph_links;
    }

    public void stampaSomme() {
        graph_nodes.forEach(n -> {
            System.out.println(n.getName() + "\tValore:" + (n.getInputSum() - n.getOutputSum()));
        });
    }
}
