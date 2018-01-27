package xdean.inject.api.annotation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.inject.Scope;

import xdean.inject.api.BeanRepository;

/**
 * Indicates the annotated element as a bean.<br>
 * 1. If annotates on class, the class will be auto-registered as bean by
 * {@link BeanRepository}.<br>
 * 2. If annotates on method, the declaring-class will be constructed and invoke for the bean.
 *
 *
 * @author XDean
 * @since 0.1
 */
@Scope
@Documented
@Retention(RUNTIME)
@Target(TYPE)
public @interface Bean {
  /**
   * Registered to which bean classes. The annotated class must extends the classes. <br>
   * For example
   *
   * <pre>
   * <code>&#64;Component(Animal.class)
   * public class Dog implements Animal{}</code>
   * </pre>
   *
   * means let the Dog be the implementation of Animal. It equals following `register` in
   * {@link BeanRepository}
   *
   * <pre>
   * <code> InjectRepository.register(Animail.class, Dog.class);</code>
   * </pre>
   */
  Class<?>[] value() default {};
}
