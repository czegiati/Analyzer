package core;

import eu.infomas.annotation.AnnotationDetector;

import java.io.IOException;
import java.lang.annotation.Annotation;
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
                    if(!getAbstractClass().isAssignableFrom(Class.forName(s))) throw new IllegalArgumentException("Classes that uses @Condition annotation should extend Abstract Condition!");

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

    default AbstractClass createInstanceOf(String name, List<AbstractClass> subobjs,Map<String,String> map){
        if(!getAbstractClassMap().containsKey(name)) throw new IllegalArgumentException("Unknown operation: "+name);

        if(!acceptAnnotationOn(this.getAbstractClassMap().get(name).getAnnotation(getAnnotationClass()),this.getAbstractClassMap().get(name),subobjs)) throw new IllegalArgumentException("The annotation was rejected!");

        Object cond0 = null;
        try {
             cond0 = getAbstractClassMap().get(name).newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        ( (AbstractClass) cond0).setSubObjects(subobjs);

        handleAttributeDependantObjects(cond0,name,map);

        return (AbstractClass) cond0;
    }

    Map<String,Class<AbstractClass>> getAbstractClassMap();

    boolean acceptAnnotationOn(Annotation annotation,Class<?> class0, List<AbstractClass> subobjs);

    Class<Anno> getAnnotationClass();

    Class<AbstractClass> getAbstractClass();

    private String getName(Class<?> class0) throws InvocationTargetException, IllegalAccessException {
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

    default void defineConst(String s,Object o, Variable c){
        c.put(s,o);
    }

    /**
     * Handles classes, that are dependant on attributes!
     */
    default void handleAttributeDependantObjects(Object cond0,String name,Map<String,String> attributes){

        if(doesClassDeriveFrom(getAbstractClassMap().get(name),Variable.class))
        {
            if(attributes.containsKey("name"))
            ((Variable) cond0).setName(attributes.get("name"));
            else throw new IllegalArgumentException("VAR should have a name attribute!");
        }

        if(doesClassDeriveFrom(getAbstractClassMap().get(name),Const.class))
        {
            if (attributes.containsKey("value"))
                ((Const) cond0).setValue(Integer.parseInt(attributes.get("value")));
            else throw new IllegalArgumentException("CONST should have a value attribute!");
        }
    }

    static boolean doesClassDeriveFrom(Class subclass,Class<?> superclass){
        return superclass.isAssignableFrom(subclass);
    }
}
