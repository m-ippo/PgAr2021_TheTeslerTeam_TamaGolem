package ttt.tamagolem.contents.xml.elements;
import ttt.utils.xml.document.XMLElement;
import ttt.utils.xml.engine.annotations.Element;

/**
 * Classe che rappresenta un particolare XMLElement.
 */
@Element(Name = "elemento", CanHaveTags = false)
public class ElementoSecondario extends XMLElement {

    public ElementoSecondario(){
        super("elemento");
    }
}
