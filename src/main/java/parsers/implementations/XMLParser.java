package parsers.implementations;

import Analyzer.core.AbstractObject;
import Analyzer.core.Analyzer;
import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import parsers.classes.AnalyzerElement;
import parsers.interfaces.AnalyzerParser;
import parsers.interfaces.ElementParser;

import java.io.File;
import java.io.IOException;
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
        try {
            File inputFile = new File(input);
            SAXBuilder saxBuilder = new SAXBuilder();
            Document document = saxBuilder.build(inputFile);
            Element root = document.getRootElement();
            return parseRoot(root,analyzer);
        } catch(JDOMException e) {
            e.printStackTrace();
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }
        throw new IllegalArgumentException("Oops, something in the file was wrong!");
    }






    static class XMLElementParser implements ElementParser<Element>{

        @Override
        public AnalyzerElement parseRootElement(Element element) {
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
