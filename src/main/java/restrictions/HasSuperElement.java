package restrictions;

import restrictions.core.RestrictionAnnotation;

import java.lang.annotation.*;


@Repeatable(HasSuperElement.List.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@RestrictionAnnotation(value = HasSuperElementRestriction.class)
public @interface HasSuperElement {
    String value();
    String message() default "Should have super element!";


    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.TYPE})
    @interface List {
        HasSuperElement[] value();
    }
}