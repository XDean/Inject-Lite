package xdean.inject.api.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import xdean.inject.api.Implementation;

@Target(value = ElementType.ANNOTATION_TYPE)
@Retention(value = RetentionPolicy.RUNTIME)
@Documented
public @interface ScopeHandler {
  Class<? extends Implementation<?>> value();
}
