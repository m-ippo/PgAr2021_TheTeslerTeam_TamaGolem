package ttt.tamagolem;

import ttt.tamagolem.contents.graph.Graph;
import ttt.tamagolem.game.MainMenu;

/**
 *
 * @author TTT
 */
public class TamaGolem {

    public static void main(String[] args) {
        //MainMenu mm = new MainMenu();
        //////// Controllare caso in cui il set di pietre è tutto uguale (3 fuoco per g1 e 3 fuoco per g2)
    }

    public static void stampaNodi(Graph g) {
        g.getGraphLinks().forEach(l -> System.out.println(l.toString()));
        g.getNodes().forEach(n -> System.out.println(n.getInputSum() + "  " + n.getOutputSum()));
    }


}
