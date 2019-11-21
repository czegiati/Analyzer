package parsers;

import core.AbstractObject;
import core.Analyzer;
import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
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
        List<Attribute> list=root.getAttributes();
        Map<String,String> map=new HashMap<>();
        for(Attribute a: list){
            map.put(a.getName(),a.getValue());
        }
        List<AbstractObject> children=new ArrayList<>();
        for(Element child:root.getChildren()){
            children.add(create(child,analyzer));

        }
        return analyzer.createInstanceOf(root.getName(),children,map);
    }


    public static AbstractObject parse(Element element,Analyzer analyzer){
        return create(element,analyzer);
    }

    public static Object get(Element element,Analyzer analyzer) {
       return create(element,analyzer).getValue();
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
    }

}
