package AnalyzerImpl.Number;

import Analyzer.core.Analyzer;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NumberAnalyzer implements Analyzer<Number,AbstractNumber> {

    public Map<String, Class<AbstractNumber>> map = new HashMap<>();

    @Override
    public Map<String, Class<AbstractNumber>> getAbstractClassMap() {
        return map;
    }

    @Override
    public boolean acceptAnnotationOn(Annotation annotation, Class<?> class0, List<AbstractNumber> subobjs) {
        Integer min = null;
        Integer max = null;
        int argn = subobjs.size();
        try {
            min = (int) annotation.getClass().getMethod("min").invoke(annotation);
            max = (int) annotation.getClass().getMethod("max").invoke(annotation);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return (min <= argn && max >= argn) || (min <= argn && max == -1);
    }


    @Override
    public Class getAnnotationClass() {
        return Number.class;
    }

    @Override
    public Class getAbstractClass() {
        return AbstractNumber.class;
    }

}
