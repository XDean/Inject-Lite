package xdean.inject.api.annotation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Indicate the annotated class defines beans.
 *
 * @author XDean
 *
 */
@Documented
@Retention(RUNTIME)
@Target(TYPE)
public @interface Scan {

}
