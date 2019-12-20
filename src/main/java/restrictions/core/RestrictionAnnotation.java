package restrictions.core;

import java.lang.annotation.*;


@Documented
@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface RestrictionAnnotation {
    Class<? extends RestrictionHandler> value();
    String defaultMessage() default "Failed instantiation of element!";
}
