package Analyzer.restrictions;

import Analyzer.restrictions.core.RestrictionAnnotation;

import java.lang.annotation.*;


@Repeatable(HasSuperElement.List.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@RestrictionAnnotation(value = HasSuperElementRestriction.class,message = "Cannot have")
public @interface HasSuperElement {
    String value();


    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.TYPE})
    @interface List {
        HasSuperElement[] value();
    }
}