package xdean.inject.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import xdean.inject.Scope;

@Target(value = ElementType.ANNOTATION_TYPE)
@Retention(value = RetentionPolicy.RUNTIME)
@Documented
public @interface ScopeHandler {
  Class<? extends Scope> value();
}
