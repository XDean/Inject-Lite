package xdean.inject.impl;

import javax.inject.Provider;

import xdean.inject.Qualifier;
import xdean.inject.Scope;

public interface BeanFactory<T> {
  Scope getScope();

  Qualifier getQualifier();

  Class<T> getType();

  Provider<T> getProvider();

  default T get() {
    return getProvider().get();
  }

  Object getIdentifier();
}
