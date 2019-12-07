package Analyzer.core;

import Analyzer.core.AttributeDependent.AttributeDependent;
import Analyzer.core.AttributeDependent.AttributeParam;
import Analyzer.core.AttributeDependent.Interceptor;
import Analyzer.core.mixed.TypeBridge;
import Analyzer.restrictions.core.RestrictionAnnotation;
import Analyzer.restrictions.core.RestrictionHandler;
import eu.infomas.annotation.AnnotationDetector;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

public interface Analyzer<Anno extends Annotation, AbstractClass extends AbstractObject> {

    List<Class<? extends Annotation>> restrictions = new ArrayList<>();

    default void detect(){
        detect(null);
    }

    default void detect(String s){
        annotationDetect(s);
    }

    private void annotationDetect(String s) {

        registerTypeBridges();
        getTypeBridgeAnalyzer("EQUALS",0);

        final AnnotationDetector.TypeReporter reporter = new AnnotationDetector.TypeReporter() {

            @Override
            public void reportTypeAnnotation(Class<? extends Annotation> aClass, String s) {
                try {
                    if (!getAbstractClass().isAssignableFrom(Class.forName(s)))
                        throw new IllegalArgumentException("Classes that uses @Condition annotation should extend Abstract Condition!");

                    String name = getName(Class.forName(s));
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
            if(s==null)
            cf.detect();
            else cf.detect(s);
        } catch (IOException e) {
        }

    }

    default AbstractClass createInstanceOf(String name, List<AbstractClass> subobjs, Map<String, String> map, List<String> superElements, Map<Integer, List<String>> subElements) {
        if (!getAbstractClassMap().containsKey(name)) throw new IllegalArgumentException("Unknown operation: " + name);

        if (!acceptAnnotationOn(getAnnotationFields(name), this.getAbstractClassMap().get(name), subobjs))
            throw new IllegalArgumentException("The annotation was rejected!");

        Object cond0 = null;
        try {
            cond0 = getAbstractClassMap().get(name).newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        ((AbstractClass) cond0).setSubObjects(subobjs);

        try {
            handleRestrictions(cond0, map, superElements, subElements);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }


        handleAttributeAnnotations(cond0, map);
        return (AbstractClass) cond0;
    }

    Map<String, Class<AbstractClass>> getAbstractClassMap();

    boolean acceptAnnotationOn(Map<String, Object> annotation, Class<?> class0, List<AbstractClass> subobjs);

    Class<Anno> getAnnotationClass();

    Class<AbstractClass> getAbstractClass();

    private String getName(Class<?> class0) throws InvocationTargetException, IllegalAccessException {
        Annotation annotation = class0.getDeclaredAnnotation(getAnnotationClass());
        for (Method f : getAnnotationClass().getMethods()) {
            if (f.getName().equals("name") && f.getReturnType().equals(String.class)) {
                Object returner = f.invoke(annotation, null);
                return (String) returner;
            }
        }
        throw new IllegalArgumentException("Your annotation should have a 'name' field!");
    }

    private void handleAttributeAnnotations(Object o, Map<String, String> attriblites) {
        if (o.getClass().isAnnotationPresent(AttributeDependent.class)) {
            Class class0 = o.getClass();
            int counter = 0;

            for (Method m : class0.getMethods()) {
                if (m.isAnnotationPresent(Interceptor.class)) {
                    counter++;
                    if (counter > 1)
                        throw new IllegalArgumentException("There can only be one Method with the Interceptor annotation!");

                    String[] params = new String[m.getParameters().length];
                    int i = 0;
                    for (Parameter p : m.getParameters()) {
                        if (p.isAnnotationPresent(AttributeParam.class)) {
                            if (attriblites.get(p.getAnnotation(AttributeParam.class).value()) == null && p.getAnnotation(AttributeParam.class).required())
                                throw new IllegalArgumentException("Attribute " + p.getAnnotation(AttributeParam.class).value() + " cannot be found, while it is required for method: " + m.getName() + " in class: " + o.getClass());
                            params[i] = attriblites.get(p.getAnnotation(AttributeParam.class).value());
                        }
                        i++;
                    }
                    try {
                        m.invoke(o, params);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
    }

    private void handleRestrictions(Object o, Map<String, String> attrs, List<String> superElements, Map<Integer, List<String>> subElements) throws IllegalAccessException, InstantiationException {
        for (Class c0 : restrictions) {
            if (o.getClass().isAnnotationPresent(c0)) { //a restriction is placed on this class
                Annotation annotation = o.getClass().getAnnotation(c0);
                if (annotation.annotationType().isAnnotationPresent(RestrictionAnnotation.class)) {
                    Class interf = annotation.annotationType().getAnnotation(RestrictionAnnotation.class).value();
                    if (RestrictionHandler.class.isAssignableFrom(interf)) {
                        ((RestrictionHandler) interf.newInstance()).accept(o, attrs, superElements, subElements, annotation);
                    } else
                        throw new IllegalArgumentException("You have to implement the RestrictionHandler interface int your restriction's body :" + interf);
                } else
                    throw new IllegalArgumentException("You cannot register an annotation as Restriction without annotating it with @RestrictionAnnotation: " + annotation.getClass());
            }
        }
    }

    static void registerRestrictions(Class<? extends Annotation>... annotation) {
        for (Class a : annotation) {
            restrictions.add(a);
        }
    }

    private Map<String,Object> getAnnotationFields(String name){
        Class annoClass=getAnnotationClass();
        Annotation a=this.getAbstractClassMap().get(name).getAnnotation(getAnnotationClass());
        Map<String,Object> map=new HashMap<>();
        for(Method f:annoClass.getDeclaredMethods()){
            try {
                map.put(f.getName(),(Object)f.invoke(a));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }

        }
        return map;
    }

    private void registerTypeBridges(){
            Method[] methods= getAbstractClass().getDeclaredMethods();
            for(Method m:methods){
                if (m.isAnnotationPresent(TypeBridge.class)){
                    this.getAbstractClassMap().put(m.getAnnotation(TypeBridge.class).name(),null);
                }
            }
    }

    default Analyzer getTypeBridgeAnalyzer(String bridgeName,int number){
        List<Method> methods= Arrays.asList(getAbstractClass().getDeclaredMethods());
        for(Method m:methods){
            if(m.isAnnotationPresent(TypeBridge.class)){
                if(m.getAnnotation(TypeBridge.class).name().equals(bridgeName))
                {
                    Class[] classes= m.getAnnotation(TypeBridge.class).analyzerClass(); //all the analyzers classes
                    Class[] paramclasses= m.getParameterTypes();
                    for(Class analyzerclass:classes){
                        Analyzer a1=null;
                        try {
                            a1= (Analyzer) analyzerclass.newInstance();
                        } catch (InstantiationException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }

                        if(a1.getAbstractClass().equals(paramclasses[number])){
                            return a1;
                        }

                    }
                }
            }
        }
        return null;
    }

     default AbstractClass getBrideType(String bridgeName,List<AbstractClass> subobjs){
        Method[] methods= getAbstractClass().getDeclaredMethods();
        for(Method m:methods){
            if(m.isAnnotationPresent(TypeBridge.class)){
                if(m.getAnnotation(TypeBridge.class).name().equals(bridgeName)) {
                    Object cond0 = null;
                    try {
                        cond0 = m.invoke(null,subobjs.toArray());
                    } catch (IllegalAccessException e) {
                        throw new IllegalArgumentException("Method should be public static: "+bridgeName);
                    } catch (InvocationTargetException e) {
                        throw new IllegalArgumentException("Method should be public static: "+bridgeName);
                    }
                    return (AbstractClass) cond0;
                }
            }
        }
        throw new IllegalArgumentException("Something did not go as planned at TypeBridge: "+bridgeName);
    }
}