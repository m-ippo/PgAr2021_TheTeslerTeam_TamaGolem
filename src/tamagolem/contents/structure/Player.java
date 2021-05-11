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
package tamagolem.contents.structure;

import tamagolem.contents.structure.golem.Golem;
import tamagolem.contents.structure.validators.NameValidator;
import ttt.utils.console.input.annotations.InputElement;
import ttt.utils.console.input.annotations.Order;
import ttt.utils.console.input.interfaces.InputObject;

/**
 * Rappresenta un giocatore.
 *
 * @author TTT
 */
public class Player implements InputObject {

    private String name;
    private Golem golem;
    private int usedGolems = 0;

    public Player() {
    }

    public Player(String name) {
        this.name = name;
    }

    @Order(Priority = 0)
    @InputElement(Name = "Nome giocatore", Type = String.class, Validator = NameValidator.class)
    public void setName(String value) {
        name = value;
    }

    public String getName() {
        return name;
    }

    public void setGolem(Golem golem) {
        this.golem = golem;
        usedGolems++;
    }

    public Golem getGolem() {
        return golem;
    }

    public int getUsedGolems() {
        return usedGolems;
    }

}
