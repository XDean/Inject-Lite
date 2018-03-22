package xdean.inject.impl.factory;

import static xdean.inject.IllegalDefineException.assertThat;
import static xdean.jex.util.lang.ExceptionUtil.uncheck;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import javax.inject.Provider;

import xdean.inject.BeanNotFoundException;
import xdean.inject.BeanRepository;
import xdean.inject.IllegalDefineException;
import xdean.inject.Qualifier;
import xdean.inject.Scope;
import xdean.jex.extra.collection.Pair;

public class FieldBeanFactory<T> extends AbstractAnnotationBeanFactory<Field, T> {

  private final ProviderTransformer<T> providerTransformer;
  private final Class<T> type;

  public FieldBeanFactory(Field element, Scope scope, Qualifier qualifier) throws IllegalDefineException {
    super(element, scope, qualifier);
    element.setAccessible(true);
    Pair<Class<T>, ProviderTransformer<T>> pair = ProviderTransformer.<T> from(element.getGenericType(), Object.class, element);
    type = pair.getLeft();
    providerTransformer = pair.getRight();
    if (providerTransformer == ProviderTransformer.FOR_INSTANCE) {
      assertThat(scope == Scope.UNDEFINED, "Field bean can't define @Scope: " + element);
    }
  }

  @Override
  public Class<T> getType() {
    return type;
  }

  @Override
  public Provider<T> getProvider(BeanRepository repo) {
    return providerTransformer.transform(() -> getFieldValue(repo));
  }

  private Object getFieldValue(BeanRepository repo) {
    if (Modifier.isStatic(element.getModifiers())) {
      return uncheck(() -> element.get(null));
    } else {
      Class<?> declaringClass = element.getDeclaringClass();
      Object owner = repo.getBean(declaringClass).orElseThrow(() -> new BeanNotFoundException(repo, declaringClass));
      return uncheck(() -> element.get(owner));
    }
  }
}
