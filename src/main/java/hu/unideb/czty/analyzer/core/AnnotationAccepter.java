package hu.unideb.czty.analyzer.core;

import hu.unideb.czty.analyzer.parsers.classes.AnalyzerElement;

import java.util.Map;

@FunctionalInterface
public interface AnnotationAccepter {
    boolean accept(Map map,Class<?> c, AnalyzerElement element,Analyzer analyzer); //a is the fields of the annotation and its values; c is the class,
}
