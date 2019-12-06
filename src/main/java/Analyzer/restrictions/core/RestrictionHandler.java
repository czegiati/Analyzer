package Analyzer.restrictions.core;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;

public interface RestrictionHandler {
    void accept(Object o, Map<String,String> attrs, List<String> superElements, Map<Integer,List<String>> subElements, Annotation annotation);

}
