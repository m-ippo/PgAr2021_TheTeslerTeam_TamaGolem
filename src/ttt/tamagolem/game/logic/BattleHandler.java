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
package ttt.tamagolem.game.logic;

import ttt.tamagolem.contents.structure.Player;
import ttt.tamagolem.contents.structure.events.GolemListener;
import ttt.tamagolem.contents.structure.golem.Golem;
import ttt.tamagolem.contents.structure.golem.Rock;
import ttt.utils.console.output.GeneralFormatter;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Gestisce le battaglie tra golems.
 *
 * @author TTT
 */
public class BattleHandler {

    private final Player g1;
    private final Player g2;

    public BattleHandler(Player g1, Player g2) {
        this.g1 = g1;
        this.g2 = g2;
    }

    /**
     * Gestisce una battaglia tra due golem. Dura finché uno dei due sfidanti
     * non muore.
     */
    public void rockBattle() {
        Golem golem1 = g1.getGolem();
        Golem golem2 = g2.getGolem();
        AtomicBoolean finished = new AtomicBoolean(false);
        GolemListener golemListener = () -> {
            finished.set(true);
        };
        golem2.addListener(golemListener);
        golem1.addListener(golemListener);

        while (!finished.get()) {
            Rock r1 = golem1.getRock();
            Rock r2 = golem2.getRock();
            if (r1.winsAgainst(r2)) {
                GeneralFormatter.incrementIndents();
                GeneralFormatter.printOut("Il golem ha subito danno pari a " + r1.against(r2), true, false);
                GeneralFormatter.decrementIndents();
                wait(1000);
                golem2.decrementLifeBy(r1.against(r2));
            } else {
                GeneralFormatter.incrementIndents();
                GeneralFormatter.printOut("Il golem ha subito danno pari a " + r2.against(r1), true, false);
                GeneralFormatter.decrementIndents();
                wait(1000);
                golem1.decrementLifeBy(r2.against(r1));
            }
        }
    }

    /**
     * Ferma il programma per la qta di millisecondi passati per parametro.
     *
     * @param millisecons Millisecondi da aspettare.
     */
    public void wait(int millisecons) {
        try {
            Thread.sleep(millisecons);
        } catch (InterruptedException e) {
        }
    }

    /**
     * Stampa punti di attesa:<br>
     * .<br>
     * ..<br>
     * ...<br>
     * Aggiungendo un punto sulla stessa riga.
     */
    public void printPuntini() {
        for (int i = 0; i < 3; i++) {
            System.out.print(". ");
            wait(500);
        }
        System.out.println();
    }

}
