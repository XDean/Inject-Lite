package xdean.inject.impl;

import xdean.inject.BeanProvider;
import xdean.inject.BeanRepository;
import xdean.inject.Qualifier;
import xdean.inject.Scope;
import xdean.inject.exception.IllegalDefineException;

public interface BeanFactory<T> {
  Scope getScope();

  Qualifier getQualifier();

  Class<T> getType();

  BeanProvider<T> getProvider(BeanRepository repo);

  default T get(BeanRepository repo) {
    return getProvider(repo).get();
  }

  Object getIdentifier();

  @Override
  int hashCode();

  @Override
  boolean equals(Object obj);

  default BeanFactory<T> validateImplements(Class<?> beanClass) {
    IllegalDefineException.assertThat(beanClass.isAssignableFrom(getType()),
        String.format("Can't convert %s to %s (%s)", getType(), beanClass, getIdentifier()));
    return this;
  }
}
