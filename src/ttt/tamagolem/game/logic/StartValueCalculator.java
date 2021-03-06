package ttt.tamagolem.game.logic;

import java.util.Objects;
import ttt.tamagolem.contents.graph.Node;
import ttt.tamagolem.contents.structure.balance.Balance;
import ttt.tamagolem.game.support.Broadcast;

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
        Broadcast.addGameValueListener((key) -> {
            if (Objects.equals(key, Broadcast.MAX_GOLEM_AMOUNT)) {
                recalc();
            }
        });
    }

    private void recalc() {
        generateRocksPerElements();
        generateCommonRocks();
        generateSumOfPower();
        generateGolemLife();
    }

    /**
     * Genera il numero di pietre.
     */
    private void generateRocksNumb() {
        this.rocks_number = (int) (Math.ceil(((double) elements_number + 1.0) / 3.0)) + 1;
        Broadcast.broadcastGameValue(Broadcast.MAX_ROCKS_PER_GOLEM, rocks_number);
    }

    /**
     * Genera il numero di TamaGolem della partita.
     */
    private void generateGolemNumber() {
        double el_num = elements_number;
        this.golems_number = (int) Math.ceil((el_num - 2.0) * (el_num - 1.0) / ((double) rocks_number * 2.0));
        Broadcast.broadcastGameValue(Broadcast.MAX_GOLEM_AMOUNT, golems_number);
    }

    /**
     * Genera il numero di pietre per ciascun elemento.
     */
    private void generateRocksPerElements() {
        double g_n = golems_number;
        double e_n = elements_number;
        double r_n = rocks_number;
        rocks_per_element = (int) Math.ceil((g_n * 2 * r_n) / e_n);
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
