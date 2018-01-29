package xdean.inject.impl.factory;

import javax.inject.Provider;

import xdean.inject.IllegalDefineException;
import xdean.inject.Qualifier;
import xdean.inject.Scope;
import xdean.inject.impl.BeanFactory;

public class ClassBeanFactory<T> implements BeanFactory<T> {
  private final Class<T> clz;
  private final Scope scope;
  private final Qualifier qualifier;

  public ClassBeanFactory(Class<T> clz, Scope scope, Qualifier qualifier) throws IllegalDefineException {
    this.clz = clz;
    this.scope = scope == Scope.DEFAULT ? Scope.from(clz) : scope;
    this.qualifier = Qualifier.from(clz).and(qualifier);
  }

  @Override
  public Scope getScope() {
    return scope;
  }

  @Override
  public Qualifier getQualifier() {
    return qualifier;
  }

  @Override
  public Provider<T> getProvider() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Object getIdentifier() {
    return clz;
  }
}
