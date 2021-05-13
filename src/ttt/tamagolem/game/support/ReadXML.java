package ttt.tamagolem.game.support;

import ttt.utils.xml.document.XMLDocument;
import ttt.utils.xml.engine.XMLEngine;
import ttt.utils.xml.io.XMLReader;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import ttt.tamagolem.contents.xml.elements.Battle;
import ttt.tamagolem.contents.xml.elements.ElementoPrincipale;
import ttt.tamagolem.contents.xml.elements.ElementoSecondario;
import ttt.tamagolem.contents.xml.elements.Nome;
import ttt.tamagolem.contents.xml.elements.Phrase;
import ttt.tamagolem.contents.xml.elements.Round;

public class ReadXML {

    /**
     * Legge e carica i nomi dei nomi dei nodi tra cui l'utente può scegliere.
     *
     * @return Il documento caricato.
     */
    public static XMLDocument loadNomi() {
        InputStream in = ReadXML.class.getResourceAsStream("/ttt/tamagolem/resources/names/Elements.xml");
        XMLReader xmlReader = new XMLReader(in);
        try {
            XMLDocument documento = xmlReader.readDocument();
            XMLEngine xmle = new XMLEngine(documento, ElementoPrincipale.class, Nome.class, ElementoSecondario.class);
            XMLDocument docConvertito = new XMLDocument(null);
            xmle.morph(docConvertito);
            return docConvertito;
        } catch (IOException ex) {
            Logger.getLogger(ReadXML.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    /**
     * Carico le frasi da dire durante la partita.
     *
     * @return Il documento caricato.
     */
    public static XMLDocument loadPhrases() {
        InputStream in = ReadXML.class.getResourceAsStream("/ttt/tamagolem/resources/phrases/phrases.xml");
        XMLReader xmlr = new XMLReader(in);
        try {
            XMLDocument doc = xmlr.readDocument();
            XMLEngine xmle = new XMLEngine(doc, Phrase.class, Battle.class, Round.class, ElementoSecondario.class);
            XMLDocument docConvertito = new XMLDocument(null);
            xmle.morph(docConvertito);
            return docConvertito;
        } catch (IOException e) {
            Logger.getLogger(ReadXML.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }
}
