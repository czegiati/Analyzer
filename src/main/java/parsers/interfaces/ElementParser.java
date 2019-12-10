package parsers.interfaces;

import parsers.classes.AnalyzerElement;

public interface ElementParser<E> {

    AnalyzerElement parseRootElement(E element);

    public default AnalyzerElement parseElement(E element, AnalyzerElement parent){
        AnalyzerElement a=parseRootElement(element);
        a.setParent(parent);
        return a;
    }
}
