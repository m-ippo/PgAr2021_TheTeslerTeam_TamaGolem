package ttt.tamagolem.contents.structure.balance;

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

    private int sumColumn(int[][] mat, int colonna) {
        int ris = 0;
        for (int[] mat1 : mat) {
            ris += mat1[colonna];
        }
        return ris;
    }

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

    /**
     * Conta le occorrenze di un valore in una riga e ne ritorna il conteggio.
     *
     * @param riga La riga da controllare.
     * @param val Il valore da cercare.
     * @return Il conteggio delle occorrenze.
     */
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
                            nuova_matrice[i][j] = random.nextInt(val_max) + (1 + countZeros(matrice[i]));
                            if (sumRow(nuova_matrice[i]) == 0) {
                                nuova_matrice[i][j] += random.nextInt(val_max / 2) + 1;
                            }
                        } else if (countOccurrences(matrice[i], -1) == 2) {
                            int somma_riga = Math.abs(sumRow(nuova_matrice[i]));
                            nuova_matrice[i][j] = random.nextInt(Math.abs(somma_riga + (somma_riga != 1 ? - 1 : -2))) + 1;
                        } else {
                            nuova_matrice[i][j] = random.nextInt(val_max) + 1;
                            if (sumRow(nuova_matrice[i]) == 0) {
                                nuova_matrice[i][j] += random.nextInt(val_max / 2) + 1;
                            }
                        }
                        nuova_matrice[j][i] = -nuova_matrice[i][j];
                        matrice[i][j] = matrice[j][i] = 7;
                    }
                }
            }
        }
        matrice = nuova_matrice;
    }
/*
    private int contaZeriColonna(int colonna) {
        int ris = 0;
        for (int i = 0; i < matrice.length; i++) {
            if (matrice[i][colonna] == 0) {
                ris++;
            }
        }
        return ris - 1;
    }

    private int contaUniColonna(int colonna) {
        int ris = 0;
        for (int i = 0; i < matrice.length; i++) {
            if (matrice[i][colonna] == -1) {
                ris++;
            }
        }
        return ris;
    }
*/
    public void generateValues2() {
        int[][] nuova_matrice = new int[matrice.length][matrice.length];
        for (int i = 0; i < matrice.length; i++) {   // crea una nuova matrice di 0 da completare
            for (int j = 0; j < matrice.length; j++) {
                nuova_matrice[i][j] = 0;
            }
        }

        for (int i = 0; i < matrice.length; i++) {
            for (int j = i + 1; j < matrice.length; j++) {
                int quanti_ne_mancano = countOccurrences(matrice[i], -1) + countZeros(matrice[i]);
                switch (quanti_ne_mancano) {
                    case 1:
                        nuova_matrice[i][j] = -sumRow(nuova_matrice[i]);
                        break;
                    case 2:
                        int valore = (random.nextInt(val_max) + 1) * (matrice[i][j] == -1 ? 1 : -1);
                        //valore = sumRow(nuova_matrice[i]) + valore == 0 ? (sumColumn(nuova_matrice, j) - valore == 0 ? -valore - (-valore == 2 ? 1 : 2) : -valore) : (sumColumn(nuova_matrice, j) + valore == 0 ? (sumRow(nuova_matrice[i]) - valore == 0 ? -valore - (-valore == 2 ? 1 : 2) : -valore) : valore);
                        valore = fixRandom(valore, i, j, nuova_matrice);
                        nuova_matrice[i][j] = valore;
                        break;
                    default:
                        nuova_matrice[i][j] = (random.nextInt(val_max) + 1) * (matrice[i][j] == -1 ? 1 : -1);
                        break;
                }
                nuova_matrice[j][i] = -nuova_matrice[i][j];
                matrice[i][j] = matrice[j][i] = 7;
            }
        }
        matrice = nuova_matrice;
    }

    /*
    Se la somma della riga e del valore generato è uguale a zero 
            Bisogna controllare se la somma del valore con segno opposto e della somma della colonna è uguale a 0
                    Se sia riga+valore ==0 e colonna-valore == 0 Il valore generato va cambiato.
                    Altrimenti va bene ritornare il valore opposto
            Altrimenti bisogna controllare se la somma del valore e della somma della colonna è uguale a 0
                    Se colonna+valore == 0 bisogna controllare se la riga-valore == 0
                        Se riga-valore == 0 il valore generato va cambiato
                        Altrimenti va bene ritornare il valore opposto.
                    Altrimenti basta ritornare il valore stesso.
     */

    private int fixRandom(int val, int riga, int colonna, int[][] n_mat) {
        if (sumRow(n_mat[riga]) + val == 0){
            if(sumColumn(n_mat, colonna) - val == 0 ){
                return fixRandom((random.nextInt(val_max) + 1) * (matrice[riga][colonna] == -1 ? 1 : -1), riga, colonna, n_mat);
            } else {
                return -val;
            }
        }  else {
            if (sumColumn(n_mat, colonna) + val == 0){
                if(sumRow(n_mat[riga]) - val == 0){
                    return fixRandom((random.nextInt(val_max) + 1) * (matrice[riga][colonna] == -1 ? 1 : -1), riga, colonna, n_mat);
                } else {
                    return -val;
                }
            } else {
                return val;
            }
        }
    }
}
