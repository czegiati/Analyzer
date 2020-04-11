package restrictions;

import core.Analyzer;
import parsers.classes.AnalyzerElement;
import restrictions.core.RestrictionHandler;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;

public class AttributeValuesRestriction implements RestrictionHandler {
    @Override
    public boolean accept(AnalyzerElement element, Object o, Analyzer analyzer, Annotation annotation) {
        String attrname=((AttributeValues)annotation).name();
        List<String> attrvalues= Arrays.asList(((AttributeValues) annotation).values());

        if(element.getAttributes().containsKey(attrname)){
            if(attrvalues.contains(element.getAttributes().get(attrname))){
                return true;
            }
        } else return true;

            return false;
    }
}
