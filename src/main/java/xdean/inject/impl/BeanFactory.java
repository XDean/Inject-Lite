package xdean.inject.impl;

import javax.inject.Provider;

import xdean.inject.BeanRepository;
import xdean.inject.IllegalDefineException;
import xdean.inject.Qualifier;
import xdean.inject.Scope;

public interface BeanFactory<T> {
  Scope getScope();

  Qualifier getQualifier();

  Class<T> getType();

  Provider<T> getProvider(BeanRepository repo);

  default T get(BeanRepository repo) {
    return getProvider(repo).get();
  }

  Object getIdentifier();

  default BeanFactory<T> validateImplements(Class<?> beanClass) {
    IllegalDefineException.assertThat(beanClass.isAssignableFrom(getType()),
        String.format("Can't convert %s to %s (%s)", getType(), beanClass, getIdentifier()));
    return this;
  }
}
