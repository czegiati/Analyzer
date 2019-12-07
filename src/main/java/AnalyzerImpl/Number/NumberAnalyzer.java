package AnalyzerImpl.Number;

import Analyzer.core.Analyzer;

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
    public boolean acceptAnnotationOn(Map annotation, Class class0, List subobjs) {
        Integer min =(int) annotation.get("min");
        Integer max = (int)annotation.get("max");
        int argn = subobjs.size();
        return (min <= argn && max >= argn) || (min <= argn && max == -1);
    }
}