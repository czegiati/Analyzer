package parsers.interfaces;

import parsers.classes.AnalyzerElement;

public interface ElementParser<E> {

    AnalyzerElement parseElement(E element);

    public default AnalyzerElement parseElement(E element, AnalyzerElement parent){
        AnalyzerElement a=parseElement(element);
        a.setParent(parent);
        return a;
    }
}
