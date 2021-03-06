package hu.unideb.czty.analyzer.core.type;

import hu.unideb.czty.analyzer.core.Analyzer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TypeBridge {
    String name();
    Class<? extends Analyzer>[] analyzerClass();
}
