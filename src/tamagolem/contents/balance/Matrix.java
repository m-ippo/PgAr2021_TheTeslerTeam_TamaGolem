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

    /**
     * Ritorna il numero di righe, e trattandosi di una matrice quadrata
     * corrisponde al numero di colonne.
     *
     * @return Il numero di righe (colonne).
     */
    public int getDimension() {
        return matrice.length;
    }

    /**
     * Ritorna la matrice associata a questa istanza. Per ogni istanza
     * corrisponde una singola matrice.
     *
     * @return La matrice (delle direzioni, oppure se il metodo {@link Matrix#generateValues()
     * } è già stato chiamato, i valori delle interazioni).
     */
    public int[][] getMatrix() {
        return matrice;
    }

    /**
     * Stampa la matrice.
     */
    public void print() {
        for (int[] matrice1 : matrice) {
            for (int j = 0; j < matrice.length; j++) {
                System.out.printf("%4d", matrice1[j]);
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

    /**
     * Somma i valori di una riga e ne restituisce il conteggio.
     *
     * @param riga La riga su cui effettuare il conto.
     * @return La somma dei valori.
     */
    private int sumRow(int[] riga) {
        int ris = 0;
        for (int i = 0; i < riga.length; i++) {
            ris += riga[i];
        }
        return ris;
    }

    /*
    private int sumColumn(int colonna) {
        int ris = 0;
        for (int[] matrice1 : matrice) {
            ris += matrice1[colonna];
        }
        return ris;
    }
     */
    /**
     * Controlla se una riga è già stata completata.
     *
     * @param riga La riga da controllare.
     * @return {@code true} se la riga è ancora da completare, in caso contrario
     * ritorna {@code false}.
     */
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

    private int countOccurrences(int[] riga, int val) {
        int ris = 0;
        for (int i = 0; i < riga.length; i++) {
            if (riga[i] == val) {
                ris++;
            }
        }
        return ris;
    }

    /**
     * Controlla se su una riga esiste un solo valore che deve essere
     * completato.
     *
     * @param riga La riga da controllare.
     * @return {@code true} nel caso rimanga solo un valore da completare.
     */
    private boolean thereIsOnlyOneOne(int[] riga) {
        return countOccurrences(riga, -1) == 1;
    }

    /**
     * Conta quanti zero sono presenti su una singola riga.
     *
     * @param riga La riga da controllare.
     * @return Il conteggio di quanti zeri rimangono nella riga.
     */
    private int countZeros(int[] riga) {
        return countOccurrences(riga, 0) - 1; // non conta quello della diagonale
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
                                nuova_matrice[l][k] = -sumRow(nuova_matrice[l]);
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
