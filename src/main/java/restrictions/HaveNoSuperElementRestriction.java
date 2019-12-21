package restrictions;

import core.Analyzer;
import restrictions.core.RestrictionHandler;
import parsers.classes.AnalyzerElement;

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
