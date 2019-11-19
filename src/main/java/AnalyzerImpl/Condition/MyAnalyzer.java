package AnalyzerImpl.Condition;

import core.Analyzer;

import java.util.HashMap;
import java.util.Map;

public class MyAnalyzer implements Analyzer<Condition, AbstractCondition> {

    public Map<String,Class<AbstractCondition>> classmap=new HashMap<>();

    @Override
    public Map<String, Class<AbstractCondition>> getAbstractClassMap() {
        return classmap;
    }

    @Override
    public boolean acceptAnnotationOn(Object... objects) {
        return true;
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
