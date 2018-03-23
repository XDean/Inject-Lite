package xdean.inject.annotation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Documented
@Retention(RUNTIME)
@Target(TYPE)
public @interface Scan {
  String[] packages() default {};

  Class<?>[] typeSafePackages() default {};

  Class<?>[] classes() default {};

  boolean autoScanCurrentPackage() default true;
}
