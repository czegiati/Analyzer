package Analyzer.core.parsers;

import Analyzer.core.AbstractObject;
import Analyzer.core.Analyzer;
import parsers.classes.AnalyzerElement;
import parsers.interfaces.AnalyzerParser;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public interface ParseWithInterface {
    default AbstractObject parseWith(ParseWith annotation, AnalyzerElement element, Analyzer analyzer){
        AnalyzerParser p=null;
        try {
            p=annotation.parser().newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return p.parseFromString(element.getContent(),analyzer);
    }

    default boolean isParseWithPresentOnMethod(Method c){
        if(c.isAnnotationPresent(ParseWith.class))
            return true;
        else return false;
    }

    default boolean isParseWithPresentOnClass(Class c){
        if(c.isAnnotationPresent(ParseWith.class))
            return  true;
        else return false;
    }


}
