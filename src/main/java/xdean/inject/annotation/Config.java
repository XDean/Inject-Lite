package xdean.inject.annotation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import xdean.inject.ClassPath;

@Documented
@Retention(RUNTIME)
@Target(TYPE)
public @interface Config {

  boolean autoRegister();

  Class<? extends ClassPath>[] classPaths() default {};
}
