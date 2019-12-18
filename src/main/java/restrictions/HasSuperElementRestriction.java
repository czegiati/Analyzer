package restrictions;

import core.Analyzer;
import restrictions.core.RestrictionHandler;
import parsers.classes.AnalyzerElement;

import java.lang.annotation.Annotation;

public class HasSuperElementRestriction implements RestrictionHandler {


    @Override
    public boolean accept(AnalyzerElement element, Object o, Analyzer analyzer, Annotation annotation) {
       if(element.getSuperElementNames().contains(((HasSuperElement)annotation).value())){
           return true;
       }
       return false;
    }
}
