package tamagolem.game.logic;

import tamagolem.contents.graph.Node;
import tamagolem.contents.structure.balance.Balance;
import tamagolem.game.support.Broadcast;

/**
 * Classe per la creazione dei valori necessari per poter iniziare la partita.
 */
class StartValueCalculator {

    private final int elements_number; // N
    private int rocks_number; // P
    private int golem_life;  // V
    private int golems_number; // G
    private int common_rocks; // S
    private int sum_of_power; // W
    private int rocks_per_element;
    private final Balance balance;

    public StartValueCalculator(Balance balance) {
        this.elements_number = balance.getNodes().size();
        this.balance = balance;
        init();
    }

    private void init() {
        generateRocksNumb();
        generateGolemNumber();
        generateRocksPerElements();
        generateCommonRocks();
        generateSumOfPower();
        generateGolemLife();
    }

    /*
    public int getElements_number() {
        return elements_number;
    }

    public int getRocks_number() {
        return rocks_number;
    }

    public int getGolem_life() {
        return golem_life;
    }

    public int getGolems_number() {
        return golems_number;
    }

    public int getCommon_rocks() {
        return common_rocks;
    }

    public int getSum_of_power() {
        return sum_of_power;
    }

    public int getRocks_per_element() {
        return rocks_per_element;
    }

    public Balance getBalance() {
        return balance;
    }
     */
    /**
     * Genera il numero di pietre.
     */
    private void generateRocksNumb() {
        this.rocks_number = Math.round(((float) (elements_number + 1) / 3) + 1);
        Broadcast.broadcastGameValue(Broadcast.MAX_ROCKS_PER_GOLEM, rocks_number);
    }

    /**
     * Genera il numero di TamaGolem della partita.
     */
    private void generateGolemNumber() {
        this.golems_number = Math.round((elements_number - 2) * (elements_number - 1) / (rocks_number * 2));
        Broadcast.broadcastGameValue(Broadcast.MAX_GOLEM_AMOUNT, golems_number);
    }

    /**
     * Genera il numero di pietre per ciascun elemento.
     */
    private void generateRocksPerElements() {
        this.rocks_per_element = Math.round((float) golems_number * 2 / (float) elements_number * (float) rocks_number);
        Broadcast.broadcastGameValue(Broadcast.MAX_ROCKS_PER_ELEMENT, rocks_per_element);
    }

    /**
     * Genera il numero di pietre della scorta comune.
     */
    private void generateCommonRocks() {
        this.common_rocks = rocks_per_element * elements_number;
        Broadcast.broadcastGameValue(Broadcast.COMMON_ROCKS_AVAILABLE, common_rocks);
    }

    /**
     * Genera la somma della potenza dei valori della pietre.
     */
    private void generateSumOfPower() {
        int sum = 0;
        for (Node n : balance.getNodes()) {
            Integer val = n.getOutputSum();
            sum += val == null ? 0 : val;
        }
        this.sum_of_power = sum;
    }

    /**
     * Genera la vita di ciascun TamaGolem.
     */
    private void generateGolemLife() {
        this.golem_life = this.sum_of_power;
        Broadcast.broadcastGameValue(Broadcast.MAX_GOLEM_LIFE, golem_life);
    }

    @Override
    public String toString() {
        return "N = " + elements_number + "\nP = " + rocks_number + "\nV = " + golem_life + "\nG = " + golems_number
                + "\nS = " + common_rocks + "\nW = " + sum_of_power + "\nNumero pietre per elemento = " + rocks_per_element;
    }
}
