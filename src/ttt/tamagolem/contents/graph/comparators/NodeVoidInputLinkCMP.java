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
package ttt.tamagolem.contents.graph.comparators;

import java.util.Comparator;
import ttt.tamagolem.contents.graph.Node;

/**
 * Controlla quale tra i due nodi ha più nodi vuoti.
 *
 * @author TTT
 */
public class NodeVoidInputLinkCMP implements Comparator<Node> {

    @Override
    public int compare(Node o1, Node o2) {
        long val1 = o1.getVoidInputLinksCount();
        long val2 = o2.getVoidInputLinksCount();
        if(val1 == 1){
            return 1;
        }
        if(val2 == 1){
            return -1;
        }
        return new NodeOutputsCMP().compare(o1, o2);
    }

}
