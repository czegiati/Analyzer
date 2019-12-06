package Analyzer.restrictions;

import Analyzer.restrictions.core.RestrictionAnnotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@RestrictionAnnotation(HaveNoSuperElementRestriction.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface HaveNoSuperElement {
    String value();
}
