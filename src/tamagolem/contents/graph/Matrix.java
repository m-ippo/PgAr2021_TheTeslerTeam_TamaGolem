package tamagolem.contents.graph;

import com.sun.source.tree.BreakTree;
import tamagolem.contents.balance.Balance;
import tamagolem.contents.exceptions.UnitializedException;

import java.util.ArrayList;
import java.util.Random;

public class Matrix {

    private int[][] matrice;
    private Random random = new Random();

    public Matrix (ArrayList<String> nomi, int val_max){
        Balance g = new Balance(val_max, nomi);
        try {
            g.generateLinkTable();
        } catch (UnitializedException e) {
            e.printStackTrace();
        }
        matrice = g.blabla();


    }

    public int getNum_row_colums() {
        return matrice.length;
    }

    public int[][] getMatrice() {
        return matrice;
    }

    public void setMatrice(int[][] matrice) {
        this.matrice = matrice;
    }

    public void stampa(){
        for(int i = 0; i < matrice.length; i++){
            for(int j = 0; j < matrice.length; j++){
                System.out.print(matrice[i][j] + "  ");
            }
            System.out.println();
        }

        for(int i = 0; i < matrice.length; i++){
            System.out.println("Somma riga numero " + i + " " + sommaRiga(matrice[i]));
        }

        for(int i = 0; i < matrice.length; i++){
            System.out.println("Somma colonna numero " + i + " " + sommaColonna(i));
        }
    }

    public int sommaRiga(int[] riga){
        int ris = 0;
        for(int i = 0; i < riga.length; i++){
            ris += riga[i];
        }
        return ris;
    }

    public int sommaColonna(int colonna){
        int ris = 0;
        for(int i = 0; i < matrice.length; i++){
                ris += matrice[i][colonna];
            }
        return ris;
    }


    public boolean rigaDaCompletare(int[] riga){
        int somma_zeri = 0;
        int somma_uni = 0;
        for(int i = 0; i < riga.length; i++){
            if(riga[i] == 0){
                somma_zeri++;
            } else if (riga[i] == -1){
                somma_uni++;
            }
        }
        return (somma_zeri == 2 && somma_uni == 0) || (somma_zeri == 1 && somma_uni == 1);
    }

    public int valoreCompletaRiga(int[] riga){
        /*int ris = 0;
        for(int i = 0; i < riga.length; i++){
            ris += riga[i];
        }
        return ris;*/
        return sommaRiga(riga);
    }

    public boolean thereIsOnlyOneOne(int[] riga){
        int ris = 0;
        for(int i = 0; i < riga.length; i++){
            if(riga[i] == -1) {
                ris++;
            }
        }
        return ris == 1;
    }

    public int quantiZeri(int[] riga){
        int ris = 0;
        for(int i = 0; i < riga.length; i++){
            if(riga[i] == 0) {
                ris++;
            }
        }
        return ris-1; // non conta quello della diagonale
    }

    public void daiValori(int val_max){

        int[][] nuova_matrice = new int[matrice.length][matrice.length];
        for(int i = 0; i < matrice.length; i++){
            for(int j = 0; j < matrice.length; j++){
                nuova_matrice[i][j] = 0;
            }
        }

        for(int i = 0; i < matrice.length; i++){
            for(int j = 0; j < matrice.length; j++){

                for(int l = 0; l < matrice.length; l++){
                    if(rigaDaCompletare(matrice[l])){
                        for(int k = 0; k < matrice.length; k++){
                            if( (l != k) && (matrice[l][k] == 0 || matrice[l][k] == -1)){
                                nuova_matrice[l][k] = -valoreCompletaRiga(nuova_matrice[l]);
                                nuova_matrice[k][l] = -nuova_matrice[l][k];
                                matrice[k][l] = matrice[l][k] = 7;
                            }
                        }
                    }
                }

                if(i != j){ //perche non deve dare valori sulla diagonale
                    if(matrice[i][j] == -1){
                        if(thereIsOnlyOneOne(matrice[i])){
                            nuova_matrice[i][j] = random.nextInt(val_max) + quantiZeri(matrice[i]);
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
        //return nuova_matrice;
    }
}
