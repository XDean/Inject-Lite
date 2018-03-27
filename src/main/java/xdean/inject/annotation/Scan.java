package xdean.inject.annotation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Scan the packages and the class's fields and methods for potential beans.
 *
 * @author Dean Xu (XDean@github.com)
 *
 */
@Documented
@Retention(RUNTIME)
@Target(TYPE)
public @interface Scan {
  public @interface Package {
    String name() default "";

    Class<?> type() default void.class;

    boolean inherit() default false;
  }

  Package[] packages() default {};

  Class<?>[] classes() default {};

  boolean currentPackage() default true;
}
