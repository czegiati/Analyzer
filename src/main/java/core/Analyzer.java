package core;

import core.attributes.AttributeDependent;
import core.attributes.AttributeParam;
import core.attributes.Interceptor;
import core.fields.AnalyzerElementSetter;
import core.fields.ContentSetter;
import core.parsing.ParserIgnoresChildren;
import core.type.TypeBridge;
import restrictions.core.RestrictionAnnotation;
import restrictions.core.RestrictionHandler;
import eu.infomas.annotation.AnnotationDetector;
import parsers.classes.AnalyzerElement;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.stream.Collectors;

public interface Analyzer<Anno extends Annotation, AbstractClass extends AbstractObject>  {

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

    default AbstractClass createInstanceOf(AnalyzerElement element,List<AbstractClass> subobjs) {
        String name=element.getName();
        Map<String, String> map=element.getAttributes();
        List<String> superElements=element.getSuperElementNames();
        Map<Integer, List<String>> subElements=element.getNamesOfSubElements();

        if (!getAbstractClassMap().containsKey(name)) throw new IllegalArgumentException("Unknown operation: " + name);

        if (!acceptAnnotationOn(getAnnotationFields(name), this.getAbstractClassMap().get(name), element,this))
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

            handleRestrictionsDynamically(getAbstractClassMap().get(name),element,(AbstractObject)cond0);

        ContentSetter.inject(cond0,element);
        AnalyzerElementSetter.inject(cond0,element);
        handleAttributeAnnotations(name,cond0, map);

        return (AbstractClass) cond0;
    }

    Map<String, Class<AbstractClass>> getAbstractClassMap();

    boolean acceptAnnotationOn(Map annotation,Class<?> class0, AnalyzerElement element,Analyzer analyzer);

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

    private void handleAttributeAnnotations(String name,Object o, Map<String, String> attriblites) {
        if (o.getClass().isAnnotationPresent(AttributeDependent.class)) {
            Class class0 = o.getClass();
            int counter = 0;

            for (Method m : class0.getMethods()) {
                if (m.isAnnotationPresent(Interceptor.class)) {
                        handleSimpleAttributes(o,attriblites,m);
                }
            }

        }
    }

    private void handleSimpleAttributes(Object o, Map<String, String> attriblites,Method m){

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

    private void handleBridgeAttributes(String name,Map<String, String> attriblites) {
        for (Method m : getAbstractClass().getMethods()) {
            if (m.isAnnotationPresent(Interceptor.class)) {
                if (m.getAnnotation(Interceptor.class).value().equals(name)) {
                    String[] params = new String[m.getParameters().length];
                    int i = 0;
                    for (Parameter p : m.getParameters()) {
                        if (p.isAnnotationPresent(AttributeParam.class)) {
                            if (attriblites.get(p.getAnnotation(AttributeParam.class).value()) == null && p.getAnnotation(AttributeParam.class).required())
                                throw new IllegalArgumentException("Attribute " + p.getAnnotation(AttributeParam.class).value() + " cannot be found, while it is required for method: " + m.getName() + " in a TypeBridge!");
                            params[i] = attriblites.get(p.getAnnotation(AttributeParam.class).value());
                        }
                        i++;
                    }
                    try {
                        m.invoke(null, params);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
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

     default AbstractClass getBrideType(AnalyzerElement element, List<AbstractObject> subobjs){
        AnalyzerElementSetter.injectStatic(getAbstractClass(),element);
        ContentSetter.injectStatic(getAbstractClass(),element);
         Map<String,String> attrs=element.getAttributes();
         String bridgeName=element.getName();
        Method[] methods= getAbstractClass().getDeclaredMethods();
        for(Method m:methods){
            if(m.isAnnotationPresent(TypeBridge.class)){
                if(m.getAnnotation(TypeBridge.class).name().equals(bridgeName)) {
                    Object cond0;
                    try {
                        cond0 = m.invoke(null,subobjs.toArray());
                    } catch (IllegalAccessException e) {
                        throw new IllegalArgumentException("Method should be public static: "+bridgeName);
                    } catch (InvocationTargetException e) {
                        throw new IllegalArgumentException("Method should be public static: "+bridgeName);
                    }
                    handleRestrictionsDynamically(m,element);
                   handleBridgeAttributes(bridgeName,attrs);
                    return (AbstractClass) cond0;
                }
            }
        }
        throw new IllegalArgumentException("Something did not go as planned at TypeBridge: "+bridgeName);
    }

    private static boolean isRestrictionCorrect(RestrictionAnnotation annotation){
        if(RestrictionHandler.class.isAssignableFrom(annotation.value()))
            return true;
        return false;
}

    private void handleRestrictionsDynamically(Class class0,AnalyzerElement element,AbstractObject o){
        for(Annotation anno:getRestrictionAnnotations(class0)){
            RestrictionAnnotation ra= anno.annotationType().getAnnotation(RestrictionAnnotation.class);
            if(isRestrictionCorrect(ra)){
                RestrictionHandler handler;
                try {
                    handler = ra.value().newInstance();
                    if(handler.accept(element,o,this,anno)){
                        return;
                    }
                    else throw new IllegalArgumentException(ra.message()+" caused by Restriction: "+anno.annotationType()+" in class "+class0.getName());
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

            }
            else throw new IllegalArgumentException("You have to implement the RestrictionHandler interface into your restriction's body: "+ra.value());
        }
    }

    private void handleRestrictionsDynamically(Method class0,AnalyzerElement element){
        for(Annotation anno:getRestrictionAnnotations(class0)){
            RestrictionAnnotation ra=anno.annotationType().getAnnotation(RestrictionAnnotation.class);
            if(isRestrictionCorrect(ra)){
                RestrictionHandler handler;
                try {
                    handler = ra.value().newInstance();
                    if(handler.accept(element,null,this,anno)){
                        return;
                    }
                    else throw new IllegalArgumentException(ra.message()+" caused by Restriction: "+anno.annotationType()+" in "+class0.getDeclaringClass().getName()+" / "+class0.getName()+" method!" );
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

            }
            else throw new IllegalArgumentException("You have to implement the RestrictionHandler interface into your restriction's body: "+ra.value());
        }
    }

    private static List<Annotation> getRestrictionAnnotations(Method method){
       return Arrays.stream(method.getAnnotations()).filter(o -> o.annotationType().isAnnotationPresent(RestrictionAnnotation.class)).collect(Collectors.toList());
    }

    private static List<Annotation> getRestrictionAnnotations(Class class0){
        return Arrays.stream(class0.getAnnotations()).filter(o -> o.annotationType().isAnnotationPresent(RestrictionAnnotation.class)).collect(Collectors.toList());

    }

    public default boolean areChildrenIgnored(AnalyzerElement element){
        if (getAbstractClassMap().get(element.getName()).isAnnotationPresent(ParserIgnoresChildren.class)) {
                return true;
            }
        return false;
    }


}