package parsers.Analyzer;

import Analyzer.core.AbstractObject;
import Analyzer.core.Analyzer;
import org.jdom2.Attribute;
import org.jdom2.Element;
import parsers.classes.AnalyzerElement;
import parsers.interfaces.AnalyzerParser;
import parsers.interfaces.ElementParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XMLParser implements AnalyzerParser {
    static final XMLElementParser elementParser = new XMLElementParser();
    @Override
    public ElementParser getElementParser() {
        return elementParser;
    }

    @Override
    public AbstractObject parseFromFile(String input, Analyzer analyzer) {
        return null;
    }


    public AbstractObject parse(Element element,Analyzer analyzer){
        AnalyzerElement ae=getElementParser().parseElement(element);
        return AnalyzerParser.parseElement(ae,analyzer);
    }



    static class XMLElementParser implements ElementParser<Element>{

        @Override
        public AnalyzerElement parseElement(Element element) {
            AnalyzerElement e=new AnalyzerElement();
            e.setName(element.getName());

            List<AnalyzerElement> children=new ArrayList<>();
            for(Element sube:element.getChildren()){
                children.add(parseElement(sube,e));
            }
            e.setSubElements(children);

            Map<String,String> map=new HashMap<>();
            for(Attribute a:element.getAttributes()){
                map.put(a.getName(),a.getValue());
            }

            e.setAttributes(map);
            return e;
        }
    }
}
