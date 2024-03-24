package fr.elitgaimix.models.spigot.Autowired;
import static java.lang.annotation.ElementType.*;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Classes that uses services should use this annotation
 */
@Target({FIELD,CONSTRUCTOR})
@Retention(RetentionPolicy.RUNTIME)
public @interface Autowired {

    /**
     * Name of the required injected component
     * @return
     */
    String value() default "default";

    Object instance = new Object();

}