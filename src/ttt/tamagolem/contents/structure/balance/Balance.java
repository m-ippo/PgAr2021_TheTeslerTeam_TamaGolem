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
package ttt.tamagolem.contents.structure.balance;

import ttt.tamagolem.contents.graph.Graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import ttt.tamagolem.contents.exceptions.UnitializedException;
import ttt.tamagolem.contents.graph.Link;
import ttt.tamagolem.contents.graph.Node;

/**
 * Equilibrio.
 *
 * @author TTT
 */
public class Balance extends Graph {

    private final HashMap<Node, HashMap<Node, Integer>> matrice_zeri_uni = new HashMap<>();//array che rappresenta un array di righe ed ognuno contiene colonne
    private List<Node> nodi_setup;
    private Matrix m;

    /**
     * Crea un nuovo Equilibrio: è necessario specificare la potenza casuale
     * massima e la lista di nomi dei nodi presenti.
     *
     * @param MaxPower La potenza massima.
     * @param names I nomi dei nodi.
     */
    public Balance(int MaxPower, ArrayList<String> names) {
        super(MaxPower, names);
        init();
    }

    /**
     * Inizializza l'Equilibrio.
     */
    private void init() {
        nodi_setup = getNodes();
        getNodes().forEach(n -> {
            HashMap<Node, Integer> riga_elemento = new HashMap<>();
            for (int i = 0; i < getNodes().size(); i++) {
                riga_elemento.put(getNodes().get(i), 0);
            }
            matrice_zeri_uni.put(n, riga_elemento);
        });
    }

    /**
     * Converte le interazioni tra i nodi (perciò le direzioni degli archi)
     * creando una matrice associata alle interazioni tra nodi. (I valori sono:
     * 0 se un nodo perde e -1 se un nodo vince).
     *
     * @return La matrice di valori interi che rappresenta le direzioni degli
     * archi.
     */
    public int[][] genMatrix() {
        try {
            generateLinkTable();
            getGraphLinks().forEach((link_creato) -> {
                Node from = link_creato.getFrom();
                Node to = link_creato.getTo();
                matrice_zeri_uni.get(from).put(to, 1);
                matrice_zeri_uni.get(to).put(from, 0);
            });

            int[][] matrice = new int[nodi_setup.size()][nodi_setup.size()];
            for (int i = 0; i < matrice.length; i++) {
                for (int j = 0; j < matrice.length; j++) {
                    matrice[i][j] = -matrice_zeri_uni.get(nodi_setup.get(i)).get(nodi_setup.get(j));
                }
            }
            return matrice;
        } catch (UnitializedException ex) {
            Logger.getLogger(Balance.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new int[0][0];
    }

    /**
     * Genera una nuova matrice che rappresenta le interazioni tra i nodi
     * (solamente le direzioni, perciò chi vince e chi prede).
     */
    public void generateNodeInteractions() {
        m = new Matrix(this);
        m.generateValues2();
    }

    /**
     * Genera (tramite matrice e non tramite ricerca nodi-archi come la
     * superclasse {@link Graph}) i valori della Potenza d'Iterazione tra i
     * nodi.
     */
    @Override
    public void generateLinkValues() {
        int[][] matrix = m.getMatrix();
        for (int i = 0; i < matrix.length; i++) {
            for (int j = i + 1; j < matrix.length; j++) {
                Node main = nodi_setup.get(i);
                Node secondary = nodi_setup.get(j);
                Link link = main.to(secondary);
                if (!link.isLocked()) {
                    //se j+1 == matrix.length allora sono all'ultima colonna: qui ci può essere un cambio di verso dell'arco
                    int read_val = matrix[i][j];
                    link.setPower(link.getFrom() == main ? read_val : -read_val);
                    link.lock();
                }
            }
        }
    }

    /**
     * Controlla che l'Equilibrio sia stato generato correttamente.
     *
     * @return {@code true} nel caso tutti i nodi e archi sono stati generati
     * correttamente.
     */
    public boolean checkBalance() {
        for (Node n : nodi_setup) {
            int val = n.getInputSum() - n.getOutputSum();
            int empties = n.getVoidLinks();
            int inputs = n.getInputLinks().size();
            int outputs = n.getOutputLinks().size();
            if (val != 0 || empties != 0 || inputs < 1 || outputs < 1) {
                return false;
            }
        }
        return true;
    }

    public Matrix getMatrix(){
        return this.m;
    }

}
