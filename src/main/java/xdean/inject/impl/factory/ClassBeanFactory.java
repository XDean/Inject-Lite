package xdean.inject.impl.factory;

import static xdean.inject.IllegalDefineException.assertNot;
import static xdean.inject.IllegalDefineException.assertThat;
import static xdean.jex.util.lang.ExceptionUtil.throwIt;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Provider;

import xdean.inject.BeanRepository;
import xdean.inject.IllegalDefineException;
import xdean.inject.Qualifier;
import xdean.inject.Scope;
import xdean.jex.extra.tryto.Try;
import xdean.jex.util.reflect.ReflectUtil;

public class ClassBeanFactory<T> extends AbstractAnnotationBeanFactory<Class<T>, T> {

  /**
   * Injectable constructors are annotated with {@code @Inject} and accept zero or more dependencies
   * as arguments. {@code @Inject} can apply to at most one constructor per class.<br>
   * {@code @Inject} is optional for public, no-argument constructors when no other constructors are
   * present. This enables injectors to invoke default constructors.
   */
  private final Constructor<T> constructor;
  /**
   * Injectable fields:
   * <ul>
   * <li>are annotated with {@code @Inject}.
   * <li>are not final.
   * <li>may have any otherwise valid name.</li>
   * </ul>
   */
  private final List<Field> fields;
  /**
   * Injectable methods:
   * <ul>
   * <li>are annotated with {@code @Inject}.</li>
   * <li>are not abstract.</li>
   * <li>do not declare type parameters of their own.</li>
   * <li>may return a result</li>
   * <li>may have any otherwise valid name.</li>
   * <li>accept zero or more dependencies as arguments.</li>
   * </ul>
   * <p>
   * A method annotated with {@code @Inject} that overrides another method annotated with
   * {@code @Inject} will only be injected once per injection request per instance. A method with
   * <i>no</i> {@code @Inject} annotation that overrides a method annotated with {@code @Inject}
   * will not be injected.
   */
  private final List<Method> methods;

  public ClassBeanFactory(Class<T> element, Scope scope, Qualifier qualifier) throws IllegalDefineException {
    super(element, scope, qualifier);
    this.constructor = initConstructor();
    this.fields = initFields();
    this.methods = initMethods();
  }

  @Override
  public Class<T> getType() {
    return element;
  }

  @Override
  public Provider<T> getProvider(BeanRepository repo) {
    return scope.transform(() -> construct(repo));
  }

  @SuppressWarnings("unchecked")
  private Constructor<T> initConstructor() throws IllegalDefineException {
    List<Constructor<?>> injects = Arrays.stream(element.getDeclaredConstructors())
        .filter(c -> c.isAnnotationPresent(Inject.class))
        .collect(Collectors.toList());
    assertThat(injects.size() < 2, "Bean class can't have more than 1 @Inject constructor: " + element);
    return (Constructor<T>) injects.stream()
        .findFirst()
        .orElseGet(() -> Try.to(() -> element.getDeclaredConstructor())
            .getOrElse(() -> throwIt(new IllegalDefineException(
                "Bean class has no @Inject constructor neither no-arg constructor: " + element))));
  }

  private List<Field> initFields() throws IllegalDefineException {
    return Arrays.stream(ReflectUtil.getAllFields(element, false))
        .filter(f -> f.isAnnotationPresent(Inject.class))
        .map(f -> assertNot(f, Modifier.isFinal(f.getModifiers()), "@Inject field can't be final: " + f))
        .collect(Collectors.toList());
  }

  private List<Method> initMethods() throws IllegalDefineException {
    return Util.getTopInstanceMethods(element)
        .stream()
        .filter(m -> m.isAnnotationPresent(Inject.class))
        .collect(Collectors.toList());
  }

  private T construct(BeanRepository repo) {
    return null;
  }
}
