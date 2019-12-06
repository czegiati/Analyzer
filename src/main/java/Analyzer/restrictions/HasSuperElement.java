package Analyzer.restrictions;

import Analyzer.restrictions.core.RestrictionAnnotation;

import java.lang.annotation.*;


@Repeatable(HasSuperElement.List.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@RestrictionAnnotation(HasSuperElementRestriction.class)
public @interface HasSuperElement {
    String value();
    int level() default -1; // -1 in in any level


    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.TYPE})
    @interface List {
        HasSuperElement[] value();
    }
}