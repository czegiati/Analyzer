package core;

import condition.AbstractCondition;
import condition.Condition;
import eu.infomas.annotation.AnnotationDetector;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

public interface Analyzer<Anno extends Annotation, AbstractClass extends AbstractObject> {

    default void annotationDetect(){
        final AnnotationDetector.TypeReporter reporter = new AnnotationDetector.TypeReporter() {

            @Override
            public void reportTypeAnnotation(Class<? extends Annotation> aClass, String s) {
                try {
                    if(!AbstractCondition.class.isAssignableFrom(Class.forName(s))) throw new IllegalArgumentException("Classes that uses @Condition annotation should extend Abstract Condition!");

                    String name =getName(Class.forName(s));
                    getAbstractClassMap().put(name, (Class<AbstractClass>) Class.forName(s));
                } catch (ClassNotFoundException e) {
                } catch (IllegalAccessException e) {
                } catch (InvocationTargetException e) {
                }
            }

            @SuppressWarnings("unchecked")
            @Override
            public Class<? extends Annotation>[] annotations() {
                return new Class[]{getAnnotationClass()};
            }

        };
        final AnnotationDetector cf = new AnnotationDetector(reporter);
        try {
            cf.detect();
        } catch (IOException e) {
        }

    }

    default AbstractClass createInstanceOf(String name, List<AbstractClass> subobjs){
        if(!getAbstractClassMap().containsKey(name)) throw new IllegalArgumentException("Unknown operation: "+name);
        if(!acceptAnnotationOn()) throw new IllegalArgumentException("The annotation was rejected!");
        Object cond0 = null;
        try {
            cond0 = getAbstractClassMap().get(name).newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        ( (AbstractClass) cond0).setSubObjects(subobjs);
        return (AbstractClass) cond0;
    }

    Map<String,Class<AbstractClass>> getAbstractClassMap();

    boolean acceptAnnotationOn(Object... objects);

    Class<Anno> getAnnotationClass();

    default public String getName(Class<?> class0) throws InvocationTargetException, IllegalAccessException {
        Annotation annotation = class0.getDeclaredAnnotation(getAnnotationClass());
        for(Method f: getAnnotationClass().getMethods())
        {
            if(f.getName().equals("name") && f.getReturnType().equals(String.class))
            {
                Object returner=f.invoke(annotation,null);
                return (String)returner;
            }
        }
        throw new IllegalArgumentException("Your annotation should have a 'name' field!");
    }

}
