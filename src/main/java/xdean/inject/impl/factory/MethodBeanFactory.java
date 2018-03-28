package xdean.inject.impl.factory;

import static xdean.inject.exception.BeanNotFoundException.notFound;
import static xdean.jex.util.lang.ExceptionUtil.uncheck;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import xdean.inject.BeanProvider;
import xdean.inject.BeanRepository;
import xdean.inject.Qualifier;
import xdean.inject.Scope;
import xdean.inject.exception.IllegalDefineException;
import xdean.inject.impl.Util;
import xdean.jex.extra.collection.Pair;

public class MethodBeanFactory<T> extends AbstractAnnotationBeanFactory<Method, T> {

  private final ProviderTransformer<T> providerTransformer;
  private final Class<T> type;

  public MethodBeanFactory(Method element, Scope scope, Qualifier qualifier) throws IllegalDefineException {
    super(element, scope, qualifier);
    IllegalDefineException.assertThat(element.getReturnType() != void.class,
        "Bean provider method must have return value: " + element);
    element.setAccessible(true);
    Pair<Class<T>, ProviderTransformer<T>> pair = ProviderTransformer.<T> from(element.getGenericReturnType(), Object.class,
        element);
    type = pair.getLeft();
    providerTransformer = pair.getRight();
  }

  @Override
  public Class<T> getType() {
    return type;
  }

  @Override
  public BeanProvider<T> getProviderActual(BeanRepository repo) {
    return scope.transform(BeanProvider.create(providerTransformer.transform(() -> getMethodValue(repo))));
  }

  private Object getMethodValue(BeanRepository repo) {
    Object[] args = Util.prepareArgument(element, repo);
    if (Modifier.isStatic(element.getModifiers())) {
      return uncheck(() -> element.invoke(null, args));
    } else {
      Class<?> declaringClass = element.getDeclaringClass();
      Object owner = repo.getBean(declaringClass).orElseThrow(notFound(repo, declaringClass));
      return uncheck(() -> element.invoke(owner, args));
    }
  }
}
