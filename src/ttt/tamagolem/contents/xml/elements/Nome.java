package ttt.tamagolem.contents.xml.elements;

import ttt.utils.engines.enums.MethodType;
import ttt.utils.engines.interfaces.EngineMethod;
import ttt.utils.xml.document.XMLElement;
import ttt.utils.xml.engine.annotations.Element;
import ttt.utils.xml.engine.annotations.Tag;

/**
 * Classe che rappresenta un particolare XMLElement.
 */
@Element(Name = "nomi",CanHaveValue = false)
public class Nome extends XMLElement {
    String tagType;
    public Nome() {
        super("nomi");
    }

    public String getType(){
        return tagType;
    }
    @EngineMethod(MethodType = MethodType.SET)
    @Tag(Name = "type")
    public void setType(String type){
        tagType=type;
    }
}
