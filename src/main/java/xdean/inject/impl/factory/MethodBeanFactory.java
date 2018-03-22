package xdean.inject.impl.factory;

import static xdean.inject.BeanNotFoundException.notFound;
import static xdean.jex.util.lang.ExceptionUtil.uncheck;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;

import javax.inject.Provider;

import xdean.inject.BeanRepository;
import xdean.inject.IllegalDefineException;
import xdean.inject.Qualifier;
import xdean.inject.Scope;
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
  public Provider<T> getProvider(BeanRepository repo) {
    return providerTransformer.transform(() -> getMethodValue(repo));
  }

  private Object getMethodValue(BeanRepository repo) {
    Object[] args = prepareArgument(repo);
    if (Modifier.isStatic(element.getModifiers())) {
      return uncheck(() -> element.invoke(null, args));
    } else {
      Class<?> declaringClass = element.getDeclaringClass();
      Object owner = repo.getBean(declaringClass).orElseThrow(notFound(repo, declaringClass));
      return uncheck(() -> element.invoke(owner, args));
    }
  }

  private Object[] prepareArgument(BeanRepository repo) {
    Parameter[] parameters = element.getParameters();
    Object[] params = new Object[element.getParameterCount()];
    for (int i = 0; i < params.length; i++) {
      Parameter p = parameters[i];
      Qualifier q = Qualifier.from(p);
      Object o = repo.query(p.getType()).qualifies(q).get().orElseThrow(notFound(repo, p.getType(), q));
      params[i] = o;
    }
    return params;
  }
}
