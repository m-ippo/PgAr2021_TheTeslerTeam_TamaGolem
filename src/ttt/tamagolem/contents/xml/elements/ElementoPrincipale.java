package ttt.tamagolem.contents.xml.elements;
import ttt.utils.xml.document.XMLElement;
import ttt.utils.xml.engine.annotations.Element;

/**
 * Classe che rappresenta un particolare XMLElement.
 */
@Element(Name = "elementi",CanHaveValue = false,CanHaveTags = false)
public class ElementoPrincipale extends XMLElement{

    public ElementoPrincipale() {
        super("elementi");
    }

}
