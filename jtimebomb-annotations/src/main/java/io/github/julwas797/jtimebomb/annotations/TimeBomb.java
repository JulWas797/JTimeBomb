package io.github.julwas797.jtimebomb.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Adds a time-bomb clause to the class.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface TimeBomb {
    /**
     * Expiration date. ISO-8601
     *
     * @return the expiration date of the method
     */
    String value();

    /**
     * Error message. Leave empty for no message.
     *
     * @return error message printed upon exiting the JVM.
     */
    String message() default "";

    /**
     * Define a custom method used for fetching the LocalDateTime..
     * The method should be accessible (public) and static.
     * <p>
     * Syntax is defined as the following-"class:method".
     * For example, "me.Class:getLocalDateTime".
     * <p>
     * Leave blank for the default LocalDateTime.now () approach.
     *
     * @return the class and method that should be used for getting LocalDateTime in a syntax: "class:method"
     */
    String timeMethod() default "";
}