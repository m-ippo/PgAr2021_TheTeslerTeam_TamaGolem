package tamagolem.contents.balance;

import java.util.Random;

public class Matrix {

    private int[][] matrice;
    private final Random random = new Random();
    private final int val_max;

    public Matrix(Balance balance) {
        matrice = balance.genMatrix();
        this.val_max = balance.getMaxPower();
    }

    public int getNumRowColums() {
        return matrice.length;
    }

    public int[][] getMatrix() {
        return matrice;
    }

    public void print() {
        for (int[] matrice1 : matrice) {
            for (int j = 0; j < matrice.length; j++) {
                System.out.printf("%4d",matrice1[j]);
            }
            System.out.println();
        }
        /*for (int i = 0; i < matrice.length; i++) {
            System.out.println("Somma riga numero " + i + " " + sumRow(matrice[i]));
        }
        for (int i = 0; i < matrice.length; i++) {
            System.out.println("Somma colonna numero " + i + " " + sumColumn(i));
        }*/
    }

    private int sumRow(int[] riga) {
        int ris = 0;
        for (int i = 0; i < riga.length; i++) {
            ris += riga[i];
        }
        return ris;
    }

    private int sumColumn(int colonna) {
        int ris = 0;
        for (int[] matrice1 : matrice) {
            ris += matrice1[colonna];
        }
        return ris;
    }

    private boolean isRowToComplete(int[] riga) {
        int somma_zeri = 0;
        int somma_uni = 0;
        for (int i = 0; i < riga.length; i++) {
            if (riga[i] == 0) {
                somma_zeri++;
            } else if (riga[i] == -1) {
                somma_uni++;
            }
        }
        return (somma_zeri == 2 && somma_uni == 0) || (somma_zeri == 1 && somma_uni == 1);
    }

    private int sumRowValue(int[] riga) {
        return sumRow(riga);
    }

    private boolean thereIsOnlyOneOne(int[] riga) {
        int ris = 0;
        for (int i = 0; i < riga.length; i++) {
            if (riga[i] == -1) {
                ris++;
            }
        }
        return ris == 1;
    }

    private int countZeros(int[] riga) {
        int ris = 0;
        for (int i = 0; i < riga.length; i++) {
            if (riga[i] == 0) {
                ris++;
            }
        }
        return ris - 1; // non conta quello della diagonale
    }

    /**
     * Metodo per assegnare i valori alla matrice, partendo dai valori di quella
     * creata dalle direzioni degli archi.
     *
     */
    public void generateValues() {
        int[][] nuova_matrice = new int[matrice.length][matrice.length];
        for (int i = 0; i < matrice.length; i++) {   // crea una nuova matrice di 0 da completare
            for (int j = 0; j < matrice.length; j++) {
                nuova_matrice[i][j] = 0;
            }
        }

        for (int i = 0; i < matrice.length; i++) {
            for (int j = 0; j < matrice.length; j++) {

                for (int l = 0; l < matrice.length; l++) { // controlla se ci sono righe da completare
                    if (isRowToComplete(matrice[l])) {
                        for (int k = 0; k < matrice.length; k++) {
                            if ((l != k) && (matrice[l][k] == 0 || matrice[l][k] == -1)) {
                                nuova_matrice[l][k] = -sumRowValue(nuova_matrice[l]);
                                nuova_matrice[k][l] = -nuova_matrice[l][k];
                                matrice[k][l] = matrice[l][k] = 7;
                            }
                        }
                    }
                }

                if (i != j) { //perche non deve dare valori sulla diagonale
                    if (matrice[i][j] == -1) {
                        if (thereIsOnlyOneOne(matrice[i])) {
                            nuova_matrice[i][j] = random.nextInt(val_max) + countZeros(matrice[i]);
                            nuova_matrice[j][i] = -nuova_matrice[i][j];
                            matrice[i][j] = matrice[j][i] = 7;
                        } else {
                            nuova_matrice[i][j] = random.nextInt(val_max) + 1;
                            nuova_matrice[j][i] = -nuova_matrice[i][j];
                            matrice[i][j] = matrice[j][i] = 7;
                        }
                    }
                }
            }
        }
        matrice = nuova_matrice;
    }
}
