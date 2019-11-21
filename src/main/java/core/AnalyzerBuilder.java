package core;

import core.AttributeHandler.AttributeHandler;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnalyzerBuilder {
    private AnnotationAccepter accepter;
    private AttributeHandler attributeHandler;
    private Class<? extends Annotation> AnnotationClass=null;
    private Class<? extends AbstractObject> AbstractClass=null;

    private Class getAnnotationClass(){
        return this.AnnotationClass;
    }

    public AnalyzerBuilder setAccepter(AnnotationAccepter accepter) {
        this.accepter = accepter;
        return this;
    }

    public AnalyzerBuilder setAbstractClass(Class<? extends AbstractObject> abstractClass) {
        AbstractClass = abstractClass;
        return this;
    }

    public AnalyzerBuilder setAnnotationClass(Class<? extends Annotation> annotationClass) {
        AnnotationClass = annotationClass;
        return this;
    }

    public AnalyzerBuilder setAttributeHandler(AttributeHandler attributeHandler){
        this.attributeHandler=attributeHandler;
        return this;
    }
    public Analyzer build(){

        return new Analyzer() {
            public Map<String,Class> map=new HashMap<>();
            @Override
            public Map<String, Class> getAbstractClassMap() {
                return map;
            }

            @Override
            public boolean acceptAnnotationOn(Annotation a, Class c, List o) {
                return accepter.accept(a,c,o);
            }

            @Override
            public Class getAnnotationClass() {
                return AnnotationClass;
            }

            @Override
            public Class getAbstractClass() {
                return AbstractClass;
            }

            @Override
            public void handleAttributeDependantObjects(Object cond0, String name, Map attributes) {
                attributeHandler.handle(cond0,name,attributes,getAbstractClassMap());
            }
        };
    }

}
