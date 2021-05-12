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
package ttt.tamagolem;

import java.util.ArrayList;
import ttt.tamagolem.contents.exceptions.UnitializedException;
import ttt.tamagolem.contents.graph.Graph;
import ttt.tamagolem.contents.graph.Node;
import ttt.tamagolem.contents.structure.balance.Balance;
import ttt.tamagolem.game.MainMenu;

/**
 *
 * @author TTT
 */
public class TamaGolem {

    public static void main(String[] args) {
        MainMenu mm = new MainMenu();
    }

    public static void stampaNodi(Graph g) {
        g.getGraphLinks().forEach(l -> System.out.println(l.toString()));
        g.getNodes().forEach(n -> System.out.println(n.getInputSum() + "  " + n.getOutputSum()));
    }

    //<editor-fold defaultstate="collapsed" desc="Tests">
    public static void provaMatrice() {
        ArrayList<String> nomi = new ArrayList<>();
        nomi.add("A");
        nomi.add("B");
        nomi.add("C");
        nomi.add("D");
        nomi.add("E");
        nomi.add("F");
        nomi.add("G");
        nomi.add("H");
        nomi.add("I");

        Balance b = new Balance(5, nomi);
        b.generateNodeInteractions();
        b.generateLinkValues();

        System.out.println("Corretto? " + b.checkBalance());
    }

    public static void provaValori() {
        ArrayList<String> nomi = new ArrayList<>();
        nomi.add("A");
        nomi.add("B");
        nomi.add("C");
        nomi.add("D");
        nomi.add("E");

        Balance b = new Balance(20, nomi);
        b.generateNodeInteractions();
        b.generateLinkValues();
        /*StartValueCalculator calcolatore = new StartValueCalculator(b);
        System.out.println(calcolatore.toString());*/
    }

    public static void provaValoriGraph() {
        ArrayList<String> nomi = new ArrayList<>();
        nomi.add("A");
        nomi.add("B");
        nomi.add("C");
        nomi.add("D");
//        nomi.add("E");
//        nomi.add("F");
//        nomi.add("G");
//        nomi.add("H");
//        nomi.add("I");

        int giusti = 0;
        for (int i = 0; i < 1000; i++) {
            Graph g = new Graph(25, nomi);
            try {
                g.generateLinkTable();
            } catch (UnitializedException e) {
                e.printStackTrace();
            }

            g.daiValori();

            boolean ris = true;

            for (Node n : g.getNodes()) {
                int in = n.getInputSum() == null ? 0 : n.getInputSum();
                int out = n.getOutputSum() == null ? 0 : n.getOutputSum();
                if (in - out != 0) {
                    ris = false;
                    g.print();
                    g.printSums();
                }
            }
            if (ris) {
                giusti++;
            }
        }

        System.out.println(giusti);

    }
//</editor-fold>

}
