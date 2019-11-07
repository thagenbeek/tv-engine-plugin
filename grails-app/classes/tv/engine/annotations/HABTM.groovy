package tv.engine.annotations

import java.lang.annotation.Documented
import java.lang.annotation.ElementType
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.lang.annotation.Target

/**
 */
@Target([ElementType.TYPE])
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface HABTM {

    /**
     * This table has a hasAndBelongsToMany with another item (or items)
     */
    String name() default "";
    String table() default "";
    String context() default "";
    String entity() default "";
    String column() default "";

}
