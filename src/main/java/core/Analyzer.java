package core;

import core.attributes.AttributeDependent;
import core.attributes.AttributeParam;
import core.attributes.Interceptor;
import core.fields.AnalyzerElementSetter;
import core.fields.ContentSetter;
import core.parsing.ParserIgnoresChildren;
import core.type.TypeBridge;
import exceptions.AbstractObjectInstantiationFailureException;
import exceptions.AnalyzerAttributeException;
import exceptions.AnalyzerDetectorException;
import exceptions.DefinedRestrictionException;
import restrictions.core.RestrictionAnnotation;
import restrictions.core.RestrictionHandler;
import eu.infomas.annotation.AnnotationDetector;
import parsers.classes.AnalyzerElement;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.stream.Collectors;

public interface Analyzer  {

    default void detect() throws AnalyzerDetectorException{
        detect(null);
    }

    default void detect(String s) throws AnalyzerDetectorException{
        annotationDetect(s);
    }

    private void annotationDetect(String s) throws AnalyzerDetectorException {

        registerTypeBridges();


            AnnotationDetector.TypeReporter reporter = new AnnotationDetector.TypeReporter() {

                @Override
                public void reportTypeAnnotation(Class<? extends Annotation> aClass, String s)  {
                    try {
                        if (!getAbstractClass().isAssignableFrom(Class.forName(s)))
                            throw new AnalyzerDetectorException("Classes that uses your annotation  should extend AbstractObject!");
                    } catch (ClassNotFoundException e) {
                    }


                    String name = null;
                    try {
                        name = getName(Class.forName(s));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        getAbstractClassMap().put(name, (Class<AbstractObject>) Class.forName(s));
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
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
            if (s == null)
                cf.detect();
            else cf.detect(s);
        }catch (Exception e){
            throw new AnalyzerDetectorException("Analyzer annotation detection failed.",e);
        }


    }

    default AbstractObject createInstanceOf(AnalyzerElement element,List<AbstractObject> subobjs) throws AbstractObjectInstantiationFailureException, DefinedRestrictionException {
        String name=element.getName();
        Map<String, String> map=element.getAttributes();
        List<String> superElements=element.getSuperElementNames();
        Map<Integer, List<String>> subElements=element.getNamesOfSubElements();

        if (!getAbstractClassMap().containsKey(name)) throw new AbstractObjectInstantiationFailureException("Unknown operation: " + name);

        if (!acceptAnnotationOn(getAnnotationFields(name), this.getAbstractClassMap().get(name), element,this))
            throw new DefinedRestrictionException("The annotation was rejected!");

        Object cond0 = null;
        try {
            cond0 = getAbstractClassMap().get(name).newInstance();
        } catch (InstantiationException e) {
            throw new AbstractObjectInstantiationFailureException("Cannot instantiate AbstractObject.",e);
        } catch (IllegalAccessException e) {
            throw new AbstractObjectInstantiationFailureException("Access modifiers prevented instantiation of AbstractObject.",e);
        }
        ((AbstractObject) cond0).setSubObjects(subobjs);

            handleRestrictionsDynamically(getAbstractClassMap().get(name),element,(AbstractObject)cond0);

        ContentSetter.inject(cond0,element);
        AnalyzerElementSetter.inject(cond0,element);
        handleAttributeAnnotations(name,cond0, map);

        return (AbstractObject) cond0;
    }

    Map<String, Class<AbstractObject>> getAbstractClassMap();

    boolean acceptAnnotationOn(Map annotation,Class<?> class0, AnalyzerElement element,Analyzer analyzer);

    @SuppressWarnings("Annotation should have RetentionPolicy.RUNTIME and its @Target should be ElementType.TYPE.")
    Class<Annotation> getAnnotationClass();

    Class<AbstractObject> getAbstractClass();

    private String getName(Class<?> class0) throws InvocationTargetException, IllegalAccessException {
        Annotation annotation = class0.getDeclaredAnnotation(getAnnotationClass());
        for (Method f : getAnnotationClass().getMethods()) {
            if (f.getName().equals("name") && f.getReturnType().equals(String.class)) {
                Object returner = f.invoke(annotation, null);
                return (String) returner;
            }
        }
        throw new AnalyzerDetectorException("Your annotation should have a 'name' field!");
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
                    throw new AnalyzerAttributeException("Attribute " + p.getAnnotation(AttributeParam.class).value() + " cannot be found, while it is required for method: " + m.getName() + " in class: " + o.getClass());
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
                                throw new AnalyzerAttributeException("Attribute " + p.getAnnotation(AttributeParam.class).value() + " cannot be found, while it is required for method: " + m.getName() + " in a TypeBridge!");
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

     default AbstractObject getBrideType(AnalyzerElement element, List<AbstractObject> subobjs){
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
                        throw new AbstractObjectInstantiationFailureException("Method should be public static: "+bridgeName,e);
                    } catch (InvocationTargetException e) {
                        throw new AbstractObjectInstantiationFailureException("Method should be public static: "+bridgeName,e);
                    }
                    handleRestrictionsDynamically(m,element);
                   handleBridgeAttributes(bridgeName,attrs);
                    return (AbstractObject) cond0;
                }
            }
        }
        throw new AbstractObjectInstantiationFailureException("Something did not go as planned at TypeBridge: "+bridgeName);
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
                    if(!handler.accept(element,o,this,anno)) {
                        if (handler.getMessage(anno) != null)
                            throw new DefinedRestrictionException(handler.getMessage(anno)+ " - in class "+class0.getName()+".");
                            else throw new DefinedRestrictionException(anno.annotationType().getAnnotation(RestrictionAnnotation.class).defaultMessage()+ " - in class "+class0.getName()+".");
                    }
                } catch (InstantiationException e) {
                    throw new DefinedRestrictionException("Exception when trying to instantiate RestrictionHandler: "+anno.annotationType().getAnnotation(RestrictionAnnotation.class).value().getName()+".");
                } catch (IllegalAccessException e) {
                    throw new DefinedRestrictionException("Cannot access accept() method in RestrictionHandler: "+anno.annotationType().getAnnotation(RestrictionAnnotation.class).value().getName()+".");
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
                    if(!handler.accept(element,null,this,anno))
                        throw new DefinedRestrictionException(handler.getMessage(anno)+ " - in method "+class0.getName()+" of "+getAbstractClass().getName()+".");
                    else throw new DefinedRestrictionException(anno.annotationType().getAnnotation(RestrictionAnnotation.class).defaultMessage()+ " - in method "+class0.getName()+" of "+getAbstractClass().getName()+".");
                } catch (InstantiationException e) {
                    throw new DefinedRestrictionException("Exception when trying to instantiate RestrictionHandler: "+anno.annotationType().getAnnotation(RestrictionAnnotation.class).value().getName()+".");
                } catch (IllegalAccessException e) {
                    throw new DefinedRestrictionException("Cannot access accept() method in RestrictionHandler: "+anno.annotationType().getAnnotation(RestrictionAnnotation.class).value().getName()+".");
                }

            }
            else throw new DefinedRestrictionException("You have to implement the RestrictionHandler interface into your restriction's body: "+ra.value());
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