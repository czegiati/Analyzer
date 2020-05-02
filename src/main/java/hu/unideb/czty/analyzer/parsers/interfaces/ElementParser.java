package hu.unideb.czty.analyzer.parsers.interfaces;

import hu.unideb.czty.analyzer.parsers.classes.AnalyzerElement;

public interface ElementParser<E> {

    AnalyzerElement parseRootElement(E element);

    public default AnalyzerElement parseElement(E element, AnalyzerElement parent){
        AnalyzerElement a=parseRootElement(element);
        a.setParent(parent);
        return a;
    }
}
