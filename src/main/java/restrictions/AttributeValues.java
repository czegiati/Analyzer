package restrictions;

import restrictions.core.RestrictionAnnotation;

import java.lang.annotation.*;

@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@RestrictionAnnotation(value = AttributeValuesRestriction.class,defaultMessage = "The attribute had the wrong value!")
@Repeatable(value = AttributeValues.List.class)
public @interface AttributeValues {
    String name();
    String[] values();

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.TYPE})
    @interface List {
        AttributeValues[] value();
    }
}
