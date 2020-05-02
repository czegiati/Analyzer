package hu.unideb.czty.analyzer.restrictions;

import hu.unideb.czty.analyzer.restrictions.core.RestrictionAnnotation;

import java.lang.annotation.*;


@Repeatable(HasSuperElement.List.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@RestrictionAnnotation(value = HasSuperElementRestriction.class)
public @interface HasSuperElement {
    String value();


    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.TYPE})
    @interface List {
        HasSuperElement[] value();
    }
}