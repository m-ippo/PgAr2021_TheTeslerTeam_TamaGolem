package tamagolem.game;

import tamagolem.contents.graph.Node;
import tamagolem.contents.structure.balance.Balance;

/**
 * Classe per la creazione dei valori necessari per poter iniziare la partita.
 */
public class StartValueCalculator {

    private int elements_number; // N
    private int rocks_number; // P
    private int golem_life;  // V
    private int golems_number; // G
    private int common_rocks; // S
    private int sum_of_power; // W
    private int rocks_per_element;
    private Balance balance;

    public StartValueCalculator(int elements_number, Balance balance){
        this.elements_number = elements_number;
        this.balance = balance;
        init();
    }

    private void init(){
        generateRocksNumb();
        generateGolemNumber();
        generateRocksPerElements();
        generateCommonRocks();
        generateSumOfPower();
        generateGolemLife();
    }

    /**
     * Genera il numero di pietre.
     */
    private void generateRocksNumb(){
        this.rocks_number = Math.round(((elements_number + 1) / 3) + 1);
    }

    /**
     * Genera il numero di TamaGolem della partita.
     */
    private void generateGolemNumber(){
        this.golems_number = Math.round((elements_number - 2) * (elements_number - 1) / (rocks_number * 2));
    }

    /**
     * Genera il numero di pietre per ciascun elemento.
     */
    private void generateRocksPerElements(){
        this.rocks_per_element = Math.round(golems_number * 2 / elements_number * rocks_number );
    }

    /**
     * Genera il numero di pietre della scorta comune.
     */
    private void generateCommonRocks(){
        this.common_rocks = rocks_per_element * elements_number;
    }

    /**
     * Genera la somma della potenza dei valori della pietre.
     */
    private void generateSumOfPower(){
        int sum = 0;
        for(Node n : balance.getNodes()){
            sum += n.getOutputSum() == null ? 0 : n.getOutputSum();
        }
        this.sum_of_power = sum;
    }

    /**
     * Genera la vita di ciascun TamaGolem.
     */
    private void generateGolemLife(){
        this.golem_life = this.sum_of_power;
    }
}
