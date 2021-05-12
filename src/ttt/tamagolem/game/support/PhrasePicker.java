package ttt.tamagolem.game.support;

import ttt.tamagolem.contents.xml.elements.Battle;
import ttt.tamagolem.contents.xml.elements.Phrase;
import ttt.tamagolem.contents.xml.elements.Round;
import ttt.utils.console.output.ObjectOutputEngine;
import ttt.utils.console.output.interfaces.PrintableObject;
import ttt.utils.xml.document.XMLDocument;
import ttt.utils.xml.engine.interfaces.IXMLElement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;

public class PhrasePicker {

    private static Stack<String> lista_battle;
    private static Stack<String> lista_round;
    private static ArrayList<String> lista_generazione;
    private static PhrasePicker instance;

    private PhrasePicker(){
        lista_battle = new Stack<>();
        lista_round = new Stack<>();
        init();
    }

    public static PhrasePicker getInstance(){
        if(instance == null){
            instance = new PhrasePicker();
        }
        return instance;
    }

    private void init(){
        XMLDocument doc = ReadXML.loadPhrases();
        Phrase p = (Phrase) doc.getFirstElement("phrases");

        Battle b = (Battle) p.getFirstElement("battle");
        for(IXMLElement ixml : b.getElements()){
            lista_battle.add(ixml.getValue());
        }
        Collections.shuffle(lista_battle);

        Round r = (Round) p.getFirstElement("round");
        for(IXMLElement ixml : r.getElements()){
            lista_round.add(ixml.getValue());
        }
        Collections.shuffle(lista_round);

    }

    public String r(Object ... po){
        String temp = lista_round.pop();
        lista_round.add(0, temp);
        return  ObjectOutputEngine.printAll(temp, po);
    }
}
