package parsers.Analyzer;


import AnalyzerImpl.Condition.AbstractCondition;
import AnalyzerImpl.Condition.ConditionAnalyzer;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class XMLParser {

   /* public static AbstractCondition parse(String inputPath){
        try {
            File inputFile = new File(inputPath);
            SAXBuilder saxBuilder = new SAXBuilder();
            Document document = saxBuilder.build(inputFile);
            Element root = document.getRootElement();
            return create(root);
        } catch(JDOMException e) {
            e.printStackTrace();
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }
        throw new IllegalArgumentException("Oops, something in the file was wrong!");
    }

    private static AbstractCondition create(Element root){
        List<AbstractCondition> children=new ArrayList<>();
        for(Element child:root.getChildren()){
            children.add(create(child));

        }
        ConditionAnalyzer a=new ConditionAnalyzer();
        return a.createInstanceOf(root.getName(),children,null);
    }

    public static AbstractCondition parse(String input,String location){
        try {
            File inputFile = new File(input);
            SAXBuilder saxBuilder = new SAXBuilder();
            Document document = saxBuilder.build(inputFile);
            Element root = document.getRootElement();
            if(root.getName().equals(getFirstTag(location)))
            return create(getElementAt(root,cutPath(location)));
            else
                throw new IllegalArgumentException("Wrong root element!");
        } catch(JDOMException e) {
            e.printStackTrace();
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }
        throw new IllegalArgumentException("Oops, something in the file was wrong!");
    }

    public static AbstractCondition parse(Element element){
        return create(element);
    }

    public static boolean get(Element element) {
       return create(element).getValue();
    }

    private static Element getElementAt(Element root,String path){
        Element current=root;
       while(path!="")
        {
            if(!path.contains("."))
            {
                return current.getChild(path).getChildren().get(0);
            }
            if(current.getChild(getFirstTag(path))!=null)
            {
                current=current.getChild(getFirstTag(path));
                path=cutPath(path);
            }
            else throw new IllegalArgumentException("Tag does not exist: "+getFirstTag(path));
        }
        return current;
    }

    private static String getFirstTag(String string){
        int i=0;
        boolean loopbroken=false;
        loop: while(i!=string.length()-1 ){
            char char0=string.charAt(i);
            if(char0=='.')
            {
                loopbroken=true;
                break loop;
            }
            i++;
        }
        if(!loopbroken) return string;
        return string.substring(0,i);
    }

    private static String cutPath(String s){
        int i=0;
        boolean loopbroken=false;
        loop: while(i!=s.length()-1 ){
            char char0=s.charAt(i);
            if(char0=='.')
            {
                loopbroken=true;
                break loop;
            }
            i++;
        }
        if(!loopbroken) return s.substring(i,s.length()+1);
        return s.substring(i+1,s.length());
    }*/

}
