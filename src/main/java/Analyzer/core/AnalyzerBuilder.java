package Analyzer.core;

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
            public Map<String,Class> map=new HashMap<>();
            @Override
            public Map<String, Class> getAbstractClassMap() {
                return map;
            }

            @Override
            public boolean acceptAnnotationOn(Map annotation, Class class0, List subobjs) {
                return accepter.accept(annotation,class0,subobjs);
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
