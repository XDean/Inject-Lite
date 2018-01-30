package xdean.inject.annotation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.inject.Scope;

/**
 * Indicate the annotated class defines beans.
 *
 * @author XDean
 *
 */
@Scope
@Documented
@Retention(RUNTIME)
@Target(TYPE)
public @interface Scan {

}
