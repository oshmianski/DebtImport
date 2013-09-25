package by.oshmianski.ui.edt;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to mark methods that require execution in EDT
 *
 * @author Eugene Matyushkin aka Skipy
 *
 * @since 13.08.2010
 *
 * @see by.oshmianski.ui.RequiresEDTPolicy
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequiresEDT {

    /**
     * Execution policy
     *
     * @return execution policy
     */
    RequiresEDTPolicy value() default RequiresEDTPolicy.ASYNC;
}