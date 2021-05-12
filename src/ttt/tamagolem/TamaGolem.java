package ttt.tamagolem;

import ttt.tamagolem.contents.graph.Graph;
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

}
