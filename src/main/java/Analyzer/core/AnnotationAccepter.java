package Analyzer.core;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;

@FunctionalInterface
public interface AnnotationAccepter {
    boolean accept(Map<String, Object> a, Class<?> c, List<Class<?>> o); //a is the fields of the annotation and its values; c is the class,
}
