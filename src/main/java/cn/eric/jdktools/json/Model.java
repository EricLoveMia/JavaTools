package cn.eric.jdktools.json;

import java.lang.annotation.*;

/**
 * Company: ClickPaaS
 *
 * @version 1.0.0
 * @description: entity class basic annotation
 * @author: qianxu
 * @date: 2022-09-06 10:19
 **/
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Model {

    /**
     * the name of class
     */
    String name() default "";

    /**
     * Date when the entity class was created.
     */
    String date() default "";

    /**
     * any comments that may want to include in the generated code.
     */
    String comments() default "";

    /**
     * Provides an example of the schema.
     **/
    String example() default "";
}
