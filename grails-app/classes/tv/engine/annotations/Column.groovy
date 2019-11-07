package tv.engine.annotations

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 */
@Target([ElementType.FIELD])
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Column {

    /**
     */
    String name() default "";

    /**
     */
    String type() default "string";

    /**
     */
    int maxLength() default 64;

    /**
     */
    boolean nullable() default true;

    /**
     */
    String context() default "";

    /**
     */
    String table() default "";


}