package cn.eric.jdktools.json;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Company: ClickPaaS
 *
 * @version 1.0.0
 * @description: prperties annotation
 * @author: qianxu
 * @date: 2022-09-06 10:03
 **/
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Property {

    /**
     * The value of the schema or property.
     **/
    String value() default "";

    /**
     * A title to explain the purpose of the schema.
     **/
    String title() default "";

    /**
     * A description of the schema.
     **/
    String description() default "";

    /**
     * Mandates that the annotated item is required or not.
     * Temporarily closed
     **/
    boolean required() default false;

}
