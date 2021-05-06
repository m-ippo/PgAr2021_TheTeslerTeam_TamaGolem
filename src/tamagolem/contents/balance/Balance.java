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
import tamagolem.contents.graph.Node;

/**
 * Equilibrio.
 *
 * @author TTT
 */
public class Balance extends Graph {

    HashMap<Node, HashMap<Node, Integer>> matrice_zeri_uni = new HashMap<>();//array che rappresenta un array di righe ed ognuno contiene colonne

    public Balance(int MaxPower, ArrayList<String> names) {
        super(MaxPower, names);
        init();
    }

    List<Node> nodi_setup;

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

    public int[][] blabla() {
        try {
            generateLinkTable();
            getGraph_links().forEach((link_creato) -> {
                Node from = link_creato.getFrom();
                Node to = link_creato.getTo();
                matrice_zeri_uni.get(from).put(to, 1);
                matrice_zeri_uni.get(to).put(from, 0);
            });

            int[][] matrice = new int[getNodes().size()][getNodes().size()];
            for(int i = 0; i < matrice.length; i++){
                for(int j = 0; j < matrice.length; j++){
                    matrice[i][j] = -matrice_zeri_uni.get(getNodes().get(i)).get(getNodes().get(j));
                }
            }

            return matrice;

//            matrice_zeri_uni.keySet().forEach(n -> {
//                matrice_zeri_uni.get(n).keySet().forEach(n2 -> {
//                    System.out.print("" + matrice_zeri_uni.get(n).get(n2) + "  ");
//                });
//                System.out.println();
//            });
        } catch (UnitializedException ex) {
            Logger.getLogger(Balance.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new int[0][0];

    }


}
