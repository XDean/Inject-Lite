package xdean.inject;

import java.util.Optional;

import javax.inject.Provider;

import xdean.inject.annotation.Bean;
import xdean.inject.annotation.Scan;

public interface BeanRepository {

  <T> BeanRegister<T> register();

  <T> BeanQuery<T> query(Class<T> beanClass);

  /**
   * Scan the class to register all potential beans.
   *
   * @see Scan
   * @see Bean
   */
  <T> void scan(Class<T> clz);

  /**
   * Register a implementation class as the bean class
   */
  default <T> void register(Class<T> beanClass, Class<? extends T> implClass) {
    this.<T> register().implementsFor(beanClass).from(implClass);
  }

  /**
   * Register a provider as the bean class
   */
  default <T> void register(Class<T> beanClass, Provider<? extends T> implProvider) {
    this.<T> register().implementsFor(beanClass).from(implProvider);
  }

  /**
   * Get bean with specific type.
   */
  default <T> Optional<T> getBean(Class<T> clz) {
    return query(clz).get();
  }

  /**
   * Get bean with specific type and name
   */
  default <T> Optional<T> getBean(Class<T> clz, String name) {
    return query(clz).named(name).get();
  }
}
