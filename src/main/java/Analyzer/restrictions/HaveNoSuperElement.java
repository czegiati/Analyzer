package Analyzer.restrictions;

import Analyzer.restrictions.core.RestrictionAnnotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@RestrictionAnnotation(value = HaveNoSuperElementRestriction.class,message = "Cannot have such super element!")
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE,ElementType.METHOD})
public @interface HaveNoSuperElement {
    String value();
}
