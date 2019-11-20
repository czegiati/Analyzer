package AnalyzerImpl.Condition;

import core.Analyzer;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConditionAnalyzer implements Analyzer<Condition, AbstractCondition> {

    public Map<String,Class<AbstractCondition>> classmap=new HashMap<>();

    @Override
    public Map<String, Class<AbstractCondition>> getAbstractClassMap() {
        return classmap;
    }

    @Override
    public boolean acceptAnnotationOn(Annotation annotation, Class<?> class0, List<AbstractCondition> subobjs) {
        Integer min=null;
        Integer max=null;
        int argn=subobjs.size();
        try {
            min= (int) annotation.getClass().getMethod("min").invoke(annotation);
            max= (int) annotation.getClass().getMethod("max").invoke(annotation);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return (min<=argn && max>=argn) || (min<=argn && max==-1);
    }

    @Override
    public Class<Condition> getAnnotationClass() {
        return Condition.class;
    }

    @Override
    public Class<AbstractCondition> getAbstractClass() {
        return AbstractCondition.class;
    }


}
