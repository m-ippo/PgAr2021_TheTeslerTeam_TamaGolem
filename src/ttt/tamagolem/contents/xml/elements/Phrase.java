package ttt.tamagolem.contents.xml.elements;

import ttt.utils.xml.document.XMLElement;
import ttt.utils.xml.engine.annotations.Element;

/**
 * Classe che rappresenta un particolare XMLElement.
 */
@Element(Name = "phrases", CanHaveTags = false, CanHaveValue = false)
public class Phrase extends XMLElement {

    public Phrase() {
        super("phrases");
    }


}
