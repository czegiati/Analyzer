package Analyzer.core;

import java.lang.annotation.Annotation;
import java.util.List;

@FunctionalInterface
public interface AnnotationAccepter {
    boolean accept(Annotation a, Class<?> c, List<Class<?>> o);
}
