package tamagolem.game;

import tamagolem.contents.xml.elements.ElementoPrincipale;
import tamagolem.contents.xml.elements.ElementoSecondario;
import tamagolem.contents.xml.elements.Nome;
import ttt.utils.xml.document.XMLDocument;
import ttt.utils.xml.engine.XMLEngine;
import ttt.utils.xml.io.XMLReader;

import java.io.File;
import java.io.IOException;

public class ReadXML {

    public static XMLDocument loadNomi(){
        String path = "src"+File.separatorChar+"tamagolem"+File.separatorChar+"resources"+File.separatorChar+"names"+File.separatorChar+"Elements.xml";
        XMLReader xmlReader = new XMLReader(new File(path));
        try {
            XMLDocument documento = xmlReader.readDocument();
            XMLEngine xmle = new XMLEngine(documento, ElementoPrincipale.class, Nome.class, ElementoSecondario.class);
            XMLDocument docConvertito = new XMLDocument(null);
            xmle.morph(docConvertito);
            return docConvertito;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
