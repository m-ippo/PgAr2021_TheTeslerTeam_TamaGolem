package Test;

import tamagolem.contents.exceptions.UnitializedException;
import tamagolem.contents.graph.Graph;
import tamagolem.contents.graph.Node;
import tamagolem.contents.structure.balance.Balance;
import tamagolem.contents.structure.golem.Rock;

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

    public static void r(){
        ArrayList<String> nomi = new ArrayList<>();
        nomi.add("A");
        nomi.add("B");
        nomi.add("C");
        nomi.add("D");
        Balance b = new Balance(36, nomi);
        b.generateNodeInteractions();
        b.generateLinkValues();

        b.print();
        System.out.println("\n\n\n");
        System.out.println(b.getNodes().get(0));
        System.out.println(b.getNodes().get(1));

        Rock r1 = new Rock(b.getNodes().get(0));
        Rock r2 = new Rock(b.getNodes().get(1));

        System.out.println(r2.winsAgainst(r1));

    }

}