package Analyzer.restrictions;

import Analyzer.core.Analyzer;
import Analyzer.restrictions.core.RestrictionHandler;
import parsers.classes.AnalyzerElement;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;

public class HaveNoSuperElementRestriction implements RestrictionHandler {


    @Override
    public boolean accept(AnalyzerElement element, Object o, Analyzer analyzer, Annotation annotation) {
        if(element.getSuperElementNames().contains(((HaveNoSuperElement)annotation).value())){
           return false;
        }
        return true;
    }


}
