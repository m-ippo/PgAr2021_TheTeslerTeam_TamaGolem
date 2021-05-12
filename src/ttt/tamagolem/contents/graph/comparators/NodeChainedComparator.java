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

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import ttt.tamagolem.contents.graph.Node;

/**
 * Catena di controlli per ordinare il completamento degli archi.
 *
 * @author TTT
 */
public class NodeChainedComparator implements Comparator<Node> {

    private final List<Comparator<Node>> comparators;

    @SafeVarargs
    public NodeChainedComparator(Comparator<Node>... comparators) {
        this.comparators = Arrays.asList(comparators);
    }

    @Override
    public int compare(Node n1, Node n2) {
        for (Comparator<Node> comparator : comparators) {
            int result = comparator.compare(n1, n2);
            if (result != 0) {
                return result;
            }
        }
        return 0;
    }
}
