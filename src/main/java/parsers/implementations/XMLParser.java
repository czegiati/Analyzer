package parsers.implementations;

import core.AbstractObject;
import core.Analyzer;
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
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XMLParser implements AnalyzerParser {


    @Override
    public AbstractObject parseFromFile(String input, Analyzer analyzer) {
        try {
            File inputFile = new File(input);
            SAXBuilder saxBuilder = new SAXBuilder();
            Document document = saxBuilder.build(inputFile);
            Element root = document.getRootElement();
            return parseRoot(new XMLElementParser().parseRootElement(root),analyzer);
        } catch(JDOMException e) {
            e.printStackTrace();
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }
        throw new IllegalArgumentException("Oops, something in the file was wrong!");
    }

    @Override
    public AbstractObject parseFromString(String str, Analyzer analyzer) {
        SAXBuilder builder = new SAXBuilder();
        Document doc = null;
        try {
            doc = builder.build(new StringReader(str));
        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return parseRoot(new XMLElementParser().parseRootElement(doc.getRootElement()),analyzer);
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

            e.setContent(element.getText().trim());

            e.setAttributes(map);
            return e;
        }
    }
}
