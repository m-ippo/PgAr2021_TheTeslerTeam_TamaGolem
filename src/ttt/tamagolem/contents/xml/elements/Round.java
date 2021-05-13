package ttt.tamagolem.contents.xml.elements;

import ttt.utils.xml.document.XMLElement;
import ttt.utils.xml.engine.annotations.Element;

/**
 * Classe che rappresenta un particolare XMLElement.
 */
@Element(Name = "round", CanHaveTags = false, CanHaveValue = false)
public class Round extends XMLElement{

    public Round(){
        super("round");
    }
}
