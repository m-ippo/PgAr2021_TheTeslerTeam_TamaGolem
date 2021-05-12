package tamagolem.game.support;

import tamagolem.contents.xml.elements.*;
import ttt.utils.xml.document.XMLDocument;
import ttt.utils.xml.engine.XMLEngine;
import ttt.utils.xml.io.XMLReader;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReadXML {

    /**
     * Legge e carica i nomi dei nomi tra cui l'utente può scegliere.
     *
     * @return Il documento caricato.
     */
    public static XMLDocument loadNomi() {
        InputStream in = ReadXML.class.getResourceAsStream("/tamagolem/resources/names/Elements.xml");
        //String path = "src" + File.separatorChar + "tamagolem" + File.separatorChar + "resources" + File.separatorChar + "names" + File.separatorChar + "Elements.xml";
        XMLReader xmlReader = new XMLReader(in /*new File(path)*/);
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

    public static XMLDocument loadPhrases(){
        InputStream in = ReadXML.class.getResourceAsStream("/tamagolem/resources/phrases/phrases.xml");
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
