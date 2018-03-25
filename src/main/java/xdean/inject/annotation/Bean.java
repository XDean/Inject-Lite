package xdean.inject.annotation;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import xdean.inject.BeanRepository;

/**
 * Indicates the annotated element as a bean.<br>
 *
 * For different element:
 * <ul>
 * <li>Class
 * <ul>
 * <li>Can't be abstract.</li>
 * <li>Both scope and qualifier are available.</li>
 * <li>Will be auto registered when the class be scanned.</li>
 * </ul>
 * </li>
 * <li>Field
 * <ul>
 * <li>Must be static final.</li>
 * <li>Only qualifier is available. Scope always be final.</li>
 * <li>Will be auto registered when the declaring-class be scanned.</li>
 * </ul>
 * </li>
 * <li>Method
 * <ul>
 * <li>Can't be abstract.</li>
 * <li>Both scope and qualifier are available.</li>
 * <li>If not static, the declaring-class will be constructed and invoke for the bean.</li>
 * <li>Will be auto registered when the declaring-class be scanned.</li>
 * </ul>
 * </li>
 * </ul>
 * 1. If annotates on class, the class will be auto-registered as bean by
 * {@link BeanRepository}.<br>
 * 2. If annotates on method, the declaring-class will be constructed and invoke for the bean.
 *
 *
 * @author XDean
 * @since 0.1
 */
@Documented
@Retention(RUNTIME)
@Target({ TYPE, FIELD, METHOD })
public @interface Bean {
  /**
   * Registered to which bean class. The annotated class must extends the classes. <br>
   * For example
   *
   * <pre>
   * <code>&#64;Component(Animal.class)
   * public class Dog implements Animal{}</code>
   * </pre>
   *
   * means let the {@code Dog} be the implementation of {@code Animal}. It equals following
   * `register` in {@link BeanRepository}
   *
   * <pre>
   * <code> InjectRepository.register(Animail.class, Dog.class);</code>
   * </pre>
   */
  Class<?>[] value() default {};

  boolean implSuperClass() default false;

  boolean implAllInterfaces() default false;
}
