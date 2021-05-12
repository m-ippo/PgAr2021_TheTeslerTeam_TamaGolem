package tamagolem.game.support;

import tamagolem.contents.structure.Player;
import tamagolem.contents.xml.elements.Phrase;
import ttt.utils.console.output.ObjectOutputEngine;
import ttt.utils.console.output.interfaces.PrintableObject;
import ttt.utils.xml.document.XMLDocument;
import ttt.utils.xml.engine.interfaces.IXMLElement;

import java.util.Collections;
import java.util.Stack;

/**
 * Classe per la gestione delle stringhe da stampare a video durante
 * la partita.
 */
public class PhrasePicker {

    private static Stack<String> lista_battle;
    private static Stack<String> lista_round;
    private static Stack<String> lista_generazione;
    private static PhrasePicker instance;

    private PhrasePicker(){
        lista_battle = new Stack<>();
        lista_round = new Stack<>();
        lista_generazione = new Stack<>();
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
        addElementToStack(lista_battle, "battle", p);
        addElementToStack(lista_round, "round", p);
        addElementToStack(lista_generazione, "generazione", p);

    }

    /**
     * Aggiunge alle stack di stringhe le frasi prese dal file xml.
     * @param s Stack a cui aggiungere le frasi.
     * @param name Nome della tipologia di frasi.
     * @param p Elenco da cui attingere.
     */
    private void addElementToStack(Stack<String> s, String name, Phrase p){
        IXMLElement ixmle = p.getFirstElement(name);
        for(IXMLElement i : ixmle.getElements()){
            s.add(i.getValue());
        }
        Collections.shuffle(s);
    }

    /**
     * Ritorna la prima frase disponibile per la tipologia di stack passata per parametro.
     * @param lista Stack da cui prendere la frase.
     * @param po Oggetti da sostituire nelle frasi (se presenti).
     * @return Stringa con valori aggiornati.
     */
    private String getFirstStringOfList(Stack<String> lista, PrintableObject... po){
        String temp = lista.pop();
        lista.add(0, temp);
        return  ObjectOutputEngine.printAll(temp, po);
    }

    /**
     * Ritorna la frase per la battaglia.
     * @return Frase per la battaglia.
     */
    public String getBattleString(){
        return getFirstStringOfList(lista_battle);
    }

    /**
     * Ritorna la frase per la generazione del tamagolem.
     * @return Frase per la generazione del tamagolem.
     */
    public String getGenerationString(){
        return getFirstStringOfList(lista_generazione);
    }

    /**
     * Ritorna la frase per il round, sostituendo il nome del giocatore danneggiato e il valore del danno.
     * @param p Giocatore il cui golem ha subito danni.
     * @param damage Valore del danno.
     * @return Frase per il round.
     */
    public String getRoundString(Player p, int damage){
        String temp = getFirstStringOfList(lista_round, p);
        return String.format(temp, damage);
    }

}
