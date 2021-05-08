package tamagolem.contents.xml.elements;

import ttt.utils.xml.document.XMLElement;
import ttt.utils.xml.engine.annotations.Element;
import ttt.utils.xml.engine.annotations.Tag;

@Element(Name = "nomi",CanHaveValue = false)
public class Nome extends XMLElement {
    String tagType;
    public Nome() {
        super("nomi");
    }

    public String getType(){
        return tagType;
    }
    @Tag(Name = "type")
    public void setType(String type){
        tagType=type;
    }
}
