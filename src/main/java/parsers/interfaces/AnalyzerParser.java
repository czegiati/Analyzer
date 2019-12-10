package parsers.interfaces;

import Analyzer.core.AbstractObject;
import Analyzer.core.Analyzer;
import org.jdom2.Element;
import parsers.classes.AnalyzerElement;

import java.util.ArrayList;
import java.util.List;

public interface AnalyzerParser {
    public ElementParser getElementParser();

    public AbstractObject parseFromFile(String input, Analyzer analyzer);

    private AbstractObject parseElement(AnalyzerElement element,Analyzer analyzer){
        if(analyzer.getAbstractClassMap().get(element.getName())==null) //if typebridge
        {
            List<AbstractObject> children=new ArrayList<>();
            int i=0;
            for(AnalyzerElement child:element.getSubElements()){
                children.add(parseElement(child,analyzer.getTypeBridgeAnalyzer(element.getName(),i)));
                i++;
            }
            return analyzer.getBrideType(element.getName(),children,element.getAttributes());
        }
        return analyzer.createInstanceOf(element.getName(),element.getSubElements(),element.getAttributes(),element.getSuperElements(),element.getNamesOfSubElements());

    }

    public default AbstractObject parseRoot(Element element, Analyzer analyzer){
        AnalyzerElement ae=getElementParser().parseRootElement(element);
        return parseElement(ae,analyzer);
    }
}
