package core;

import eu.infomas.annotation.AnnotationDetector;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConditionAnalyzer  {
    public static Map<String, Class<?>> conditionMap=new HashMap<>();

    public static void annotationDetect(){
        final AnnotationDetector.TypeReporter reporter = new AnnotationDetector.TypeReporter() {

            @Override
            public void reportTypeAnnotation(Class<? extends Annotation> aClass, String s) {
                try {
                    if(!AbstractCondition.class.isAssignableFrom(Class.forName(s))) throw new IllegalArgumentException("Classes that uses @Condition annotation should extend Abstract Condition!");


                    String name =getName(Class.forName(s));
                    conditionMap.put(name,Class.forName(s));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }

            @SuppressWarnings("unchecked")
            @Override
            public Class<? extends Annotation>[] annotations() {
                return new Class[]{Condition.class};
            }

        };
        final AnnotationDetector cf = new AnnotationDetector(reporter);
        try {
            cf.detect();
        } catch (IOException e) {
        }

    }

    public static AbstractCondition createInstanceOf(String name,List<AbstractCondition> subConds) {
        if(!conditionMap.containsKey(name)) throw new IllegalArgumentException("Unknown condition: "+name);
        if((subConds.size()< getParamMin(name)|| (subConds.size()> getParamMax(name) &&getParamMax(name)!=-1))
                || (getParamMax(name)==-1 && subConds.size()< getParamMin(name))) throw new IllegalArgumentException(name+ "Condition should have  "+getParamMin(name)+" - "+(getParamMax(name)==-1?"any":getParamMax(name))+" parameters!");
        Object cond0 = null;
        try {
            cond0 = conditionMap.get(name).newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        AbstractCondition cond = (AbstractCondition) cond0;
        cond.setSubConditions(subConds);
        return cond;
    }

    public static String getName(Class class0){
                   Condition c =(Condition) class0.getAnnotation(Condition.class);
       return c.name();
    }

    public static String getClassName(Class class0){
        return class0.getName();
    }

    public static int getParamMin(String name){
        Condition c=(Condition) conditionMap.get(name).getAnnotation(Condition.class);
        return c.min();
    }

    public static int getParamMax(String name){
        Condition c=(Condition) conditionMap.get(name).getAnnotation(Condition.class);
        return c.max();
    }




    }
