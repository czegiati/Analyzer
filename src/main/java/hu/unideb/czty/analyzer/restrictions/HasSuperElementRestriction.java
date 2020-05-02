package hu.unideb.czty.analyzer.restrictions;

import hu.unideb.czty.analyzer.core.Analyzer;
import hu.unideb.czty.analyzer.restrictions.core.RestrictionHandler;
import hu.unideb.czty.analyzer.parsers.classes.AnalyzerElement;

import java.lang.annotation.Annotation;

public class HasSuperElementRestriction implements RestrictionHandler {


    @Override
    public boolean accept(AnalyzerElement element, Object o, Analyzer analyzer, Annotation annotation) {
       if(element.getSuperElementNames().contains(((HasSuperElement)annotation).value())){
           return true;
       }
       return false;
    }

    @Override
    public String getMessage(Annotation annotation) {
        return ((HasSuperElement)annotation).value()+" should be super element of this!";
    }
}
