package parsers.interfaces;

import core.AbstractObject;
import core.Analyzer;
import parsers.classes.AnalyzerElement;

import java.util.ArrayList;
import java.util.List;

public interface AnalyzerParser {
    public ElementParser getElementParser();

    public AbstractObject parseFromFile(String input, Analyzer analyzer);

    public AbstractObject parseFromString(String str,Analyzer analyzer);

    private AbstractObject parseElement(AnalyzerElement element,Analyzer analyzer){
        List<AbstractObject> children=new ArrayList<>();
        int i=0;

        if(!analyzer.getAbstractClassMap().containsKey(element.getName())) throw new IllegalArgumentException(element.getName()+" has not been created!");

            if (analyzer.getAbstractClassMap().get(element.getName()) == null) {
                for (AnalyzerElement child : element.getSubElements()) {
                    children.add(parseElement(child, analyzer.getTypeBridgeAnalyzer(element.getName(), i)));
                    i++;
                }
            } else {
                if(!analyzer.areChildrenIgnored(element))
                {
                    for (AnalyzerElement child : element.getSubElements())
                    {
                        children.add(parseElement(child, analyzer));
                    }
                }
            }
        if (analyzer.getAbstractClassMap().get(element.getName()) == null) //if typebridge
        {
             return analyzer.getBrideType(element, children);
         }

        return analyzer.createInstanceOf(element,children);

    }

    public default AbstractObject parseRoot(AnalyzerElement element, Analyzer analyzer){
        return parseElement(element,analyzer);
    }
}
