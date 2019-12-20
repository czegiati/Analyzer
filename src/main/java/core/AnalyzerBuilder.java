package core;

import parsers.classes.AnalyzerElement;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnalyzerBuilder {
    private AnnotationAccepter accepter;
    private Class<? extends Annotation> AnnotationClass=null;
    private Class<? extends AbstractObject> AbstractClass=null;

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

    public Analyzer build(){

        return new Analyzer() {
            public Map<String,Class<AbstractObject>> map=new HashMap<>();
            @Override
            public Map<String, Class<AbstractObject>> getAbstractClassMap() {
                return map;
            }

            @Override
            public boolean acceptAnnotationOn(Map map,Class class0, AnalyzerElement element,Analyzer analyzer) {
                return accepter.accept(map,class0,element,analyzer);
            }


            @Override
            public Class getAnnotationClass() {
                return AnnotationClass;
            }

            @Override
            public Class getAbstractClass() {
                return AbstractClass;
            }

        };
    }

}
