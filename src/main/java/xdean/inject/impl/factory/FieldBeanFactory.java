package xdean.inject.impl.factory;

import static xdean.jex.util.lang.ExceptionUtil.uncheck;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Optional;

import javax.inject.Provider;

import xdean.inject.BeanRepository;
import xdean.inject.IllegalDefineException;
import xdean.inject.Qualifier;
import xdean.inject.Scope;

public class FieldBeanFactory<T> extends AbstractAnnotationBeanFactory<Field, T> {

  private final ProviderTransformer<T> providerTransformer;

  public FieldBeanFactory(Field element, Scope scope, Qualifier qualifier) throws IllegalDefineException {
    super(element, scope, qualifier);
    element.setAccessible(true);
    providerTransformer = ProviderTransformer.<T> from(element.getGenericType(), Object.class, element);
  }

  private Object getFieldValue(BeanRepository repo) {
    if (Modifier.isStatic(element.getModifiers())) {
      return uncheck(() -> element.get(null));
    } else {
      Optional<?> owner = repo.getBean(element.getDeclaringClass());
    }
    return null;
  }

  @Override
  @SuppressWarnings("unchecked")
  public Class<T> getType() {
    return (Class<T>) element.getType();
  }

  @Override
  public Provider<T> getProvider(BeanRepository repo) {
    return providerTransformer.transform(() -> getFieldValue(repo));
  }
}
