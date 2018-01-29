package xdean.inject;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

import javax.inject.Provider;

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
  <S extends T> void from(Class<S> clz) throws IllegalDefineException;

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
  <S extends T> void from(Provider<S> provider) throws IllegalDefineException;

  BeanRegister<T> implementsFor(Class<? super T> clz);

  @SuppressWarnings("unchecked")
  default BeanRegister<T> implementsFor(Class<? super T>... classes) {
    Arrays.stream(classes).forEach(this::implementsFor);
    return this;
  }

  default BeanRegister<T> named(String name) {
    return qualifies(Qualifier.named(name));
  }

  BeanRegister<T> qualifies(Qualifier qualifier);

  /**
   * Set scope of the bean, override the original scope.
   */
  BeanRegister<T> scope(Scope scope);
}
