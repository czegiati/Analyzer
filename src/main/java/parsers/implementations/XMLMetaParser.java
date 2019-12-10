package parsers.implementations;

import Analyzer.core.AbstractObject;
import Analyzer.core.Analyzer;
import org.jdom2.*;
import org.jdom2.input.SAXBuilder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XMLMetaParser {

    public static AbstractObject parse(String inputPath,Analyzer analyzer){
        try {
            File inputFile = new File(inputPath);
            SAXBuilder saxBuilder = new SAXBuilder();
            Document document = saxBuilder.build(inputFile);
            Element root = document.getRootElement();
            return parse(root,analyzer);
        } catch(JDOMException e) {
            e.printStackTrace();
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }
        throw new IllegalArgumentException("Oops, something in the file was wrong!");
    }

    private static AbstractObject create(Element root, Analyzer analyzer){

        if(analyzer.getAbstractClassMap().get(root.getName())==null) //if typebridge
        {
            List<AbstractObject> children=new ArrayList<>(); //children of current - CHANGE
            int i=0;
            for(Element child:root.getChildren()){
                children.add(create(child,analyzer.getTypeBridgeAnalyzer(root.getName(),i)));
                i++;
            }

            Map<String,String> attributes=new HashMap<>(); // attributes of current
            for(Attribute a: root.getAttributes()){
                attributes.put(a.getName(),a.getValue());
            }

            return analyzer.getBrideType(root.getName(),children,attributes);
        }

        Map<String,String> attributes=new HashMap<>(); // attributes of current
        for(Attribute a: root.getAttributes()){
            attributes.put(a.getName(),a.getValue());
        }

        List<AbstractObject> children=new ArrayList<>(); //children of current
        for(Element child:root.getChildren()){
            children.add(create(child,analyzer));
        }

        List<String> superTags=new ArrayList<>();
        Element current=root;
        while(current.getParentElement()!=null) {
            current=current.getParentElement();
            superTags.add(current.getName());
        }

        Map<Integer,List<String>> subTags=new HashMap<>();
        List<Element> toBeExamined=root.getChildren();
        int level=0;
        while(!toBeExamined.isEmpty()){
            List<String> tags=new ArrayList<>();
            List<Element> newElements=new ArrayList<>();
            for(Element e: toBeExamined)
            {
                tags.add(e.getName());
                newElements.addAll(e.getChildren());
            }
            subTags.put(level,tags);
            toBeExamined=newElements;
            level++;
        }

        return analyzer.createInstanceOf(root.getName(),children,attributes,superTags,subTags);
    }


    public static AbstractObject parse(Element element,Analyzer analyzer){
        return create(element,analyzer);
    }

    public static Object get(Element element,Analyzer analyzer) {
       return create(element,analyzer).getValue();
    }

    public static Element getDom2Element(String inputPath){
       try{
                File inputFile = new File(inputPath);
                SAXBuilder saxBuilder = new SAXBuilder();
                Document document = saxBuilder.build(inputFile);
               return  document.getRootElement();
            } catch(JDOMException e) {
                e.printStackTrace();
            } catch(IOException ioe) {
                ioe.printStackTrace();
            }
            throw new IllegalArgumentException("Oops, something in the file was wrong!");
        }

}
