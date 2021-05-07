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

import tamagolem.contents.graph.Graph;

import java.util.ArrayList;
import tamagolem.contents.structure.balance.Balance;
import tamagolem.game.MainMenu;

/**
 *
 * @author TTT
 */
public class TamaGolem {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        MainMenu mm = new MainMenu();
        //provaMatrice();
    }

    public static void stampaNodi(Graph g) {
        g.getGraphLinks().forEach(l -> System.out.println(l.toString()));
        g.getNodes().forEach(n -> System.out.println(n.getInputSum() + "  " + n.getOutputSum()));
    }

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
}
