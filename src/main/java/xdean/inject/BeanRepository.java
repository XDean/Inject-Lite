package xdean.inject;

import java.util.Optional;
import java.util.function.Consumer;

import xdean.inject.annotation.Config;

public interface BeanRepository {
  /**
   * Configure this repository by annotation
   *
   * @see Config
   */
  BeanRepository config(Class<?> config);

  /**
   * Configure this repository
   */
  BeanRepository config(Consumer<BeanRepositoryConfig> config);

  <T> BeanRegister<T> register();

  <T> BeanQuery<T> query(Class<T> beanClass);

  /**
   * Scan the class to register all potential beans and configuration.
   */
  BeanRepository scan(Class<?> clz);

  /**
   * Scan the package to register all potential beans and configuration.
   */
  BeanRepository scan(String... packages);

  /**
   * Register a bean class
   */
  default <T> BeanRepository register(Class<T> beanClass) {
    this.<T> register().from(beanClass);
    return this;
  }

  /**
   * Register a implementation class as the bean class
   */
  default <T> BeanRepository register(Class<? super T> beanClass, Class<T> implClass) {
    this.<T> register().implementsFor(beanClass).from(implClass);
    return this;
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
