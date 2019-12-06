package Executor;

import Analyzer.restrictions.HasSuperElementRestriction;
import Analyzer.restrictions.core.RestrictionAnnotation;

import java.lang.annotation.*;

@Repeatable(ExecutorParam.List.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@RestrictionAnnotation(HasSuperElementRestriction.class)
public @interface ExecutorParam {
    Class value(); //expected class
    int number() default -1; // number of given param, unlimited by default



    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.TYPE})
    @interface List {
        ExecutorParam[] value();
    }
}
