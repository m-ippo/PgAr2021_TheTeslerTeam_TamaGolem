package ttt.tamagolem.contents.xml.elements;

import ttt.utils.xml.document.XMLElement;
import ttt.utils.xml.engine.annotations.Element;

@Element(Name = "battle", CanHaveValue = false, CanHaveTags = false)
public class Battle extends XMLElement {

    public Battle(){
        super("battle");
    }
}
