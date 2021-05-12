package Test;

import ttt.tamagolem.contents.exceptions.UnitializedException;
import ttt.tamagolem.contents.graph.Graph;
import ttt.tamagolem.contents.graph.Node;
import ttt.tamagolem.contents.structure.balance.Balance;

import java.util.ArrayList;

public class Test {

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
        for (int i = 0; i < 1000; i++){
            Graph g = new Graph(25, nomi);
            try {
                g.generateLinkTable();
            } catch (UnitializedException e) {
                e.printStackTrace();
            }

            g.daiValori();

            boolean ris = true;

            for(Node n : g.getNodes()){
                int in = n.getInputSum() == null ? 0 : n.getInputSum();
                int out = n.getOutputSum() == null ? 0 : n.getOutputSum();
                if(in - out != 0){
                    ris = false;
                    g.print();
                    g.printSums();
                }
            }
            if(ris){
                giusti++;
            }
        }

        System.out.println(giusti);


    }

}
