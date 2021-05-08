package tamagolem.game.support;

import tamagolem.contents.xml.elements.ElementoPrincipale;
import tamagolem.contents.xml.elements.ElementoSecondario;
import tamagolem.contents.xml.elements.Nome;
import ttt.utils.xml.document.XMLDocument;
import ttt.utils.xml.engine.XMLEngine;
import ttt.utils.xml.io.XMLReader;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReadXML {

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
}
