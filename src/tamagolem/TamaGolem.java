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

/**
 *
 * @author TTT
 */
public class TamaGolem {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

    }

    public static void provaCreazione() {

        ArrayList<String> nomi = new ArrayList<String>();
        nomi.add("A");
        nomi.add("B");
        nomi.add("C");
        nomi.add("D");

        Graph g = new Graph(20, nomi);
        g.generateLinkValues();
    }
}
