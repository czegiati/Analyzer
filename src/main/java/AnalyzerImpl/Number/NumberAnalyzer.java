package AnalyzerImpl.Number;

import core.Analyzer;
import parsers.classes.AnalyzerElement;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NumberAnalyzer implements Analyzer {
    static Map<String,Class> map=new HashMap<>();
    @Override
    public Map<String, Class> getAbstractClassMap() {
        return map;
    }

    @Override
    public Class getAnnotationClass() {
        return Number.class;
    }

    @Override
    public Class getAbstractClass() {
        return AbstractNumber.class;
    }

    @Override
    public boolean acceptAnnotationOn(Map annotation, Class class0, AnalyzerElement element,Analyzer analyzer) {
        Integer min =(int) annotation.get("min");
        Integer max = (int)annotation.get("max");
        int argn = element.getSubElements().size();
        return (min <= argn && max >= argn) || (min <= argn && max == -1);
    }
}
