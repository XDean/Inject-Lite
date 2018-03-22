package xdean.inject;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

import javax.inject.Provider;

import xdean.inject.exception.IllegalDefineException;

/**
 * Helper for register bean in repository.
 *
 * @author XDean
 *
 * @param <T>
 */
public interface BeanRegister<T> {

  /**
   * Register bean from the class
   */
  void from(Class<T> clz) throws IllegalDefineException;

  /**
   * Register bean from the field
   */
  void from(Field field) throws IllegalDefineException;

  /**
   * Register bean from the method
   */
  void from(Method method) throws IllegalDefineException;

  /**
   * Register bean from the provider
   */
  void from(Provider<T> provider) throws IllegalDefineException;

  /**
   * Register as the beanClass's implementation.
   */
  BeanRegister<T> implementsFor(Class<? super T> beanClass);

  @SuppressWarnings("unchecked")
  default BeanRegister<T> implementsFor(Class<? super T>... classes) {
    Arrays.stream(classes).forEach(this::implementsFor);
    return this;
  }

  default BeanRegister<T> named(String name) {
    return qualifier(Qualifier.named(name));
  }

  /**
   * Add additional qualifier
   */
  BeanRegister<T> qualifier(Qualifier qualifier);

  /**
   * Set scope of the bean, override the original scope.
   */
  BeanRegister<T> scope(Scope scope);
}
