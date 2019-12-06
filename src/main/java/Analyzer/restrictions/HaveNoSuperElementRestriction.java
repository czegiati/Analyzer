package Analyzer.restrictions;

import Analyzer.core.Analyzer;
import Analyzer.restrictions.core.RestrictionHandler;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;

public class HaveNoSuperElementRestriction implements RestrictionHandler {
    @Override
    public void accept(Object o, Map<String, String> attrs, List<String> superElements, Map<Integer, List<String>> subElements, Annotation annotation) {
        if(superElements.contains(((HaveNoSuperElement)annotation).value()))
        {
            throw new IllegalArgumentException("Should not have super element "+(((HaveNoSuperElement)annotation).value())+"!");
        }
    }
}
