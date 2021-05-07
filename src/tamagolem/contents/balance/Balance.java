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
package tamagolem.contents.balance;

import com.sun.source.tree.BreakTree;
import tamagolem.contents.graph.Graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import tamagolem.contents.exceptions.UnitializedException;
import tamagolem.contents.graph.Link;
import tamagolem.contents.graph.Node;

/**
 * Equilibrio.
 *
 * @author TTT
 */
public class Balance extends Graph {

    private HashMap<Node, HashMap<Node, Integer>> matrice_zeri_uni = new HashMap<>();//array che rappresenta un array di righe ed ognuno contiene colonne
    private List<Node> nodi_setup;

    public Balance(int MaxPower, ArrayList<String> names) {
        super(MaxPower, names);
        init();
    }

    private void init() {
        nodi_setup = getNodes();
        for (Node n : getNodes()) {
            HashMap<Node, Integer> riga_elemento = new HashMap<>();
            for (int i = 0; i < getNodes().size(); i++) {
                riga_elemento.put(getNodes().get(i), 0);
            }
            matrice_zeri_uni.put(n, riga_elemento);
        }
    }

    public int[][] genMatrix() {
        try {
            generateLinkTable();
            getGraph_links().forEach((link_creato) -> {
                Node from = link_creato.getFrom();
                Node to = link_creato.getTo();
                matrice_zeri_uni.get(from).put(to, 1);
                matrice_zeri_uni.get(to).put(from, 0);
            });

            int[][] matrice = new int[nodi_setup.size()][nodi_setup.size()];
            //System.out.println(nodi_setup);
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

    @Override
    public void generateLinkValues() {
        Matrix m = new Matrix(this);
        m.generateValues();
        int[][] matrix = m.getMatrix();
        m.print();
        for (int i = 0; i < matrix.length; i++) {
            for (int j = i + 1; j < matrix.length; j++) {
                Node main = nodi_setup.get(i);
                Node secondary = nodi_setup.get(j);
                Link link = main.to(secondary);
                int read_val = matrix[i][j];
                System.out.println(link + " LETTO:" + read_val);
                if (!link.isLocked()) {
                    //se j+1 == matrix.length allora sono all'ultima colonna: qui ci può essere un cambio di verso dell'arco
                    link.setPower(link.getFrom() == main ? read_val : -read_val);
                    link.lock();
                }
            }
        }
        print();
    }

    public boolean checkBalance() {
        for (Node n : nodi_setup) {
            int val = n.getInputSum() - n.getOutputSum();
            int empties = n.getVoidLinks();
            int inputs = n.getInputLinks().size();
            int outputs = n.getOutputLinks().size();
            if (val != 0 || empties != 0 || inputs < 1 || outputs < 1) {
                System.out.printf(n.toString() + "\nErrore nodo " + n.getName() + ":\n\tValore ris: %d\n\tVuoti rimasti:%d\n\tInputs:%d\n\tOutputs:%d", val, empties, inputs, outputs);
                return false;
            }
        }
        return true;
    }

}
