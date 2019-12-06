package Executor;

import Analyzer.core.Analyzer;
import Analyzer.restrictions.core.RestrictionAnnotation;
import eu.infomas.annotation.AnnotationDetector;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

public interface Executor {


    default void detect() {
        final AnnotationDetector.TypeReporter reporter = new AnnotationDetector.TypeReporter() {

            @Override
            public void reportTypeAnnotation(Class<? extends Annotation> aClass, String s) {
                getList().add(s);
            }

            @SuppressWarnings("unchecked")
            @Override
            public Class<? extends Annotation>[] annotations() {
                return getDetectableClasses();
            }

        };
        final AnnotationDetector cf = new AnnotationDetector(reporter);
        try {
            cf.detect();
        } catch (IOException e) {
        }
    }

    private Class<? extends Annotation>[] getDetectableClasses(){
        Class<? extends Annotation>[] array=new Class[getAnalyzers().size()+2];
        int i=0;
        for(Analyzer a: getAnalyzers()){
            array[i]=a.getAnnotationClass();
            i++;
        }
        array[getAnalyzers().size()]=ExecutorClass.class;
        array[getAnalyzers().size()+1]= RestrictionAnnotation.class;
        return array;
    }

    List<String> getList();

    List<Analyzer> getAnalyzers();

    Object invokeExecutorMethod();

    Map<String, Method> getExecutorMethods();

    default Analyzer getAnalyzerOf(String methodName, int num){
       return null;
    }


}
