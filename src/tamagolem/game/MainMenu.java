/*
 * Copyright 2021 TTT.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package tamagolem.game;

import tamagolem.contents.structure.Player;
import tamagolem.contents.xml.elements.ElementoPrincipale;
import tamagolem.contents.xml.elements.Nome;
import ttt.utils.ProjectSettings;
import ttt.utils.console.input.ObjectInputEngine;
import ttt.utils.console.menu.Menu;
import ttt.utils.console.output.GeneralFormatter;
import ttt.utils.xml.engine.interfaces.IXMLElement;

/**
 *
 * @author TTT
 */
public class MainMenu {

    private final Menu<Void> main;
    private Difficulty difficolta;
    private ElementoPrincipale elenco_nomi_disponibili;
    private Nome elenco_nomi_nodi;


    public static enum Difficulty {
        FACILE(3, 5), NORMALE(6, 8), DIFFICILE(9, 10), HARDCORE(11, 13), ESTREMA(14, 16);
        int min;
        int max;

        private Difficulty(int min, int max) {
            this.min = min;
            this.max = max;
        }

        public int getMin() {
            return min;
        }

        public int getMax() {
            return max;
        }

        @Override
        public String toString() {
            return this.name().substring(0, 1).toUpperCase() + this.name().substring(1).toLowerCase();
        }

    }

    public MainMenu() {
        this.difficolta = Difficulty.NORMALE;
        main = new Menu<>("TAMA-GOLEM") {
        };
        init();
    }

    private void init() {
        ProjectSettings.PROGRAM_DEFAULT_OBJECT_OUTPUT_PHRASE = false;
        ProjectSettings.PROGRAM_SHOW_OBJECT_INPUT = false;
        main.addOption("Impostazioni", () -> {
            GeneralFormatter.incrementIndents();
            showSettings();
            GeneralFormatter.decrementIndents();
            return null;
        });
        main.addOption("Imposta giocatori", () -> {
            GeneralFormatter.incrementIndents();
            setPlayers();
            GeneralFormatter.decrementIndents();
            return null;
        });
        //Caricare file XML e impostare valori default.
        elenco_nomi_disponibili = (ElementoPrincipale) ReadXML.loadNomi().getFirstElement("elementi");
        main.setDefaultSpaces(1);
        main.paintMenu();
    }

    private void showSettings() {
        Menu<Void> impostazioni = new Menu<Void>("{Impostazioni}") {
        };
        impostazioni.removeOption(1);
        impostazioni.autoPrintSpaces(false);
        impostazioni.addOption("Lista elementi", () -> {
            GeneralFormatter.incrementIndents();
            selectElements();
            GeneralFormatter.decrementIndents();
            return null;
        });
        impostazioni.addOption("Difficolta'", () -> {
            GeneralFormatter.incrementIndents();
            selectDifficulty();
            GeneralFormatter.decrementIndents();
            return null;
        });
        impostazioni.addOption("Indietro", () -> {
            impostazioni.quit();
            return null;
        });
        impostazioni.paintMenu();
    }

    private void selectDifficulty() {
        Menu<Difficulty> difficolta_menu = new Menu<>("{Seleziona difficolta' : " + difficolta.toString() + "}") {
        };
        difficolta_menu.removeOption(1);
        difficolta_menu.autoPrintSpaces(false);
        for (Difficulty d : Difficulty.values()) {//[FACILE,NORMALE,DIFFICILE,...]
            difficolta_menu.addOption(d.toString(), () -> {
                this.difficolta = d;
                GeneralFormatter.printOut("Difficolta' impostata a: " + d.toString(), true, false);
                difficolta_menu.quit();
                return d;
            });
        }
        difficolta_menu.addOption("Indietro", () -> {
            difficolta_menu.quit();
            return difficolta;
        });
        difficolta = difficolta_menu.showAndWait();
    }

    private void selectElements() {
        //Selezione degli elementi da quelli letti nel file XML
        Menu<Nome> selezione_nomi_elementi = new Menu<>("{Scegli nome elementi}") {};
        selezione_nomi_elementi.removeOption(1);
        selezione_nomi_elementi.autoPrintSpaces(false);
        for(IXMLElement nome_elemento : elenco_nomi_disponibili.getElements()){
            Nome opzione = (Nome) nome_elemento;
            selezione_nomi_elementi.addOption(opzione.getType(),()->{
                GeneralFormatter.printOut("I nomi degli elementi sono presi dalla lista:"+opzione.getType(),true,false);
                selezione_nomi_elementi.quit();
                return opzione;
            });
        }
        selezione_nomi_elementi.addOption("Indietro",()->{
            selezione_nomi_elementi.quit();
            return elenco_nomi_nodi;
        });
        this.elenco_nomi_nodi = selezione_nomi_elementi.showAndWait();
    }

    private void setPlayers() {
        Player player1 = ObjectInputEngine.readNewObject(Player.class, "Imposta giocatore 1");
        Player player2 = ObjectInputEngine.readNewObject(Player.class, "Imposta giocatore 2");
    }

}
