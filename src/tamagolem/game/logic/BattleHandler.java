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
package tamagolem.game.logic;

import tamagolem.contents.structure.events.GolemListener;
import tamagolem.contents.structure.golem.Golem;
import tamagolem.contents.structure.golem.Rock;
import ttt.utils.console.output.GeneralFormatter;

/**
 * Gestisce le battaglie tra golems.
 *
 * @author TTT
 */
public class BattleHandler {

    private Golem g1;
    private Golem g2;

    public BattleHandler(Golem g1, Golem g2) {
        this.g1 = g1;
        this.g2 = g2;
    }

    public void rockBattle() {
        Rock r1 = g1.getRock();
        Rock r2 = g2.getRock();
        if(r1.winsAgainst(r2)){
            g2.decrementLifeBy(r1.against(r2));
            GeneralFormatter.incrementIndents();
            GeneralFormatter.printOut("Il golem ha subito danno pari a " + r1.against(r2), true, false);
            GeneralFormatter.decrementIndents();
            wait(500);
        } else {
            g1.decrementLifeBy(r2.against(r1));
            GeneralFormatter.incrementIndents();
            GeneralFormatter.printOut("Il golem ha subito danno pari a " + r1.against(r2), true, false);
            GeneralFormatter.decrementIndents();
            wait(500);
        }
    }

    /**
     * Ferma il programma per la qta di millisecondi passati per parametro.
     * @param millisecons Millisecondi da aspettare.
     */
    public void wait(int millisecons){
        try {
            Thread.sleep(millisecons);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void printPuntini(){
        for(int i = 0; i < 3; i++) {
            System.out.print(". ");
            wait(500);
        }
        System.out.println();
    }

    public boolean aGolemIsDead(){
        return g1.isDead() || g2.isDead();
    }

    public Golem getG1() {
        return g1;
    }

    public void setG1(Golem g1) {
        this.g1 = g1;
    }

    public Golem getG2() {
        return g2;
    }

    public void setG2(Golem g2) {
        this.g2 = g2;
    }


}
