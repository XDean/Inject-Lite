package xdean.inject;

import java.util.Optional;

public interface BeanRepository {

  BeanRepository autoRegister(boolean auto);

  <T> BeanRegister<T> register();

  <T> BeanQuery<T> query(Class<T> beanClass);

  /**
   * Scan the class to register all potential beans and configuration.
   */
  <T> void scan(Class<T> clz);

  /**
   * Scan the package to register all potential beans and configuration.
   */
  <T> void scan(String... packages);

  /**
   * Register a bean class
   */
  default <T> void register(Class<T> beanClass) {
    this.<T> register().from(beanClass);
  }

  /**
   * Register a implementation class as the bean class
   */
  default <T> void register(Class<? super T> beanClass, Class<T> implClass) {
    this.<T> register().implementsFor(beanClass).from(implClass);
  }

  /**
   * Get bean with specific type.
   */
  default <T> Optional<? extends T> getBean(Class<T> clz) {
    return query(clz).get();
  }

  /**
   * Get bean with specific type and name
   */
  default <T> Optional<? extends T> getBean(Class<T> clz, String name) {
    return query(clz).named(name).get();
  }
}
