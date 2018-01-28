package xdean.inject;

import java.util.Optional;

import javax.inject.Provider;

import xdean.inject.annotation.Bean;
import xdean.inject.annotation.Scan;

public interface BeanRepository {

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
  <T> void register(Class<T> beanClz, Class<? extends T> impl);

  /**
   * Register a provider as the bean class
   */
  <T> void register(Class<T> beanClz, Provider<? extends T> impl);

  /**
   * Register a bean class as it self
   */
  default <T> void register(Class<T> beanClz) {
    register(beanClz, beanClz);
  }

  /**
   * Get bean with specific type.
   */
  <T> Optional<T> getBean(Class<T> clz);

  /**
   * Get bean with specific type and name
   */
  <T> Optional<T> getBean(Class<T> clz, String name);
}
