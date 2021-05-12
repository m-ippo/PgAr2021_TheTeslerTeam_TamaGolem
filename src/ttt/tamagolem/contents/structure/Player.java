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
package ttt.tamagolem.contents.structure;

import ttt.tamagolem.contents.structure.golem.Golem;
import ttt.tamagolem.contents.structure.validators.NameValidator;
import ttt.utils.console.input.annotations.InputElement;
import ttt.utils.console.input.annotations.Order;
import ttt.utils.console.input.interfaces.InputObject;
import ttt.utils.console.output.annotations.Printable;
import ttt.utils.console.output.interfaces.PrintableObject;

/**
 * Rappresenta un giocatore.
 *
 * @author TTT
 */
public class Player implements InputObject, PrintableObject {

    private String name;
    private Golem golem;
    private int usedGolems = 0;

    /**
     * Costruttore che serve per il motore di auto-inserimento di oggetti da
     * console.
     */
    public Player() {
    }

    /**
     * Costruttore che chiede il nome del giocatore.
     *
     * @param name
     */
    public Player(String name) {
        this.name = name;
    }

    /**
     * Imposta il nome al giocatore.
     *
     * @param value Il nome da assegnare.
     */
    @Order(Priority = 0)
    @InputElement(Name = "Nome giocatore", Type = String.class, Validator = NameValidator.class)
    public void setName(String value) {
        name = value;
    }

    /**
     * Ritorna il nome assegnato a questo giocatore.
     *
     * @return Il nome.
     */
    @Printable(replace = "giocatore")
    public String getName() {
        return name;
    }

    /**
     * Imposta il nuovo golem da usare e aumenta il conteggio dei golem usati da
     * questo giocatore.
     *
     * @param golem Il golem in uso.
     */
    public void setGolem(Golem golem) {
        this.golem = golem;
        if (golem != null) {
            usedGolems++;
        }
    }

    /**
     * Ritorna il golem in utilizzo dal giocatore.
     *
     * @return Il golem corrente.
     */
    public Golem getGolem() {
        return golem;
    }

    /**
     * Ritorna il numero di golem già usati.
     *
     * @return Il conteggio di golem usati.
     */
    public int getUsedGolems() {
        return usedGolems;
    }

    /**
     * Reimposta il giocatore per iniziare una nuova partita.
     */
    public void reset() {
        usedGolems = 0;
        golem = null;
    }

}
