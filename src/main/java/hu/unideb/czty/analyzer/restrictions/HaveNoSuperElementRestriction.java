package hu.unideb.czty.analyzer.restrictions;

import hu.unideb.czty.analyzer.core.Analyzer;
import hu.unideb.czty.analyzer.restrictions.core.RestrictionHandler;
import hu.unideb.czty.analyzer.parsers.classes.AnalyzerElement;

import java.lang.annotation.Annotation;

public class HaveNoSuperElementRestriction implements RestrictionHandler {


    @Override
    public boolean accept(AnalyzerElement element, Object o, Analyzer analyzer, Annotation annotation) {
        if(element.getSuperElementNames().contains(((HaveNoSuperElement)annotation).value())){
           return false;
        }
        return true;
    }

    @Override
    public String getMessage(Annotation annotation) {
        return ((HaveNoSuperElement)annotation).value()+" tag should not be super element of this!";
    }
}
