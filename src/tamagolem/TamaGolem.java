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
package tamagolem;

import tamagolem.contents.exceptions.UnitializedException;
import tamagolem.contents.graph.Graph;
import tamagolem.contents.graph.Matrix;

import java.util.ArrayList;

/**
 *
 * @author TTT
 */
public class TamaGolem {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        ArrayList<String> nomi = new ArrayList<String>();
        nomi.add("A");
        nomi.add("B");
        nomi.add("C");
        nomi.add("D");
        nomi.add("E");
        nomi.add("F");
        nomi.add("G");

        Matrix m = new Matrix( nomi, 25);
        m.stampa();

        System.out.println("\n\n");

        m.daiValori(25);

        System.out.println("\n\n");

        m.stampa();

    }

    public static void provaCreazione() {

        ArrayList<String> nomi = new ArrayList<String>();
        nomi.add("A");
        nomi.add("B");
        nomi.add("C");
        nomi.add("D");
        nomi.add("E");
        nomi.add("F");
        nomi.add("G");

        Graph g = new Graph(10, nomi);
        try {
            g.generateLinkTable();
        } catch (UnitializedException e) {
            e.printStackTrace();
        }
        stampaNodi(g);
        g.generateLinkValues();
        stampaNodi(g);
        g.stampaSomme();
    }

    public static void stampaNodi(Graph g) {
        g.getGraph_links().forEach(l -> System.out.println(l.toString()));
        g.getNodes().forEach(n -> System.out.println(n.getInputSum() + "  " + n.getOutputSum()));
    }
}
