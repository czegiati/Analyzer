package hu.unideb.czty.analyzer.restrictions.core;

import hu.unideb.czty.analyzer.core.Analyzer;
import hu.unideb.czty.analyzer.parsers.classes.AnalyzerElement;

import java.lang.annotation.Annotation;

public interface RestrictionHandler {

    /**
     *
     * @param element       The Analyzer tree's currently observed element.
     * @param o             Instantiated object of element parameter, if restriction is applied to a TypeBridge method, then it is null.
     * @param analyzer      Analyzer currently working on this element.
     * @param annotation    Annotation which causes this method to be called.
     * @return              False, when the observed element does not meet the expectations.
     */
    boolean accept(AnalyzerElement element, Object o, Analyzer analyzer,Annotation annotation);

    default String getMessage(Annotation annotation){
        return null;
    }

}
