package Analyzer.restrictions;

import Analyzer.core.Analyzer;
import Analyzer.restrictions.core.RestrictionHandler;
import parsers.classes.AnalyzerElement;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;

public class HasSuperElementRestriction implements RestrictionHandler {


    @Override
    public boolean accept(AnalyzerElement element, Object o, Analyzer analyzer, Annotation annotation) {
       if(element.getSuperElementNames().contains(((HasSuperElement)annotation).value())){
           return true;
       }
       return false;
    }
}
