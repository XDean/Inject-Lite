package xdean.inject.impl.factory;

import javax.inject.Provider;

import xdean.inject.BeanRepository;
import xdean.inject.Qualifier;
import xdean.inject.Scope;
import xdean.inject.impl.BeanFactory;

@Deprecated
public class ProviderBeanFactory<T> implements BeanFactory<T> {

  protected final Provider<T> provider;
  protected final Scope scope;
  protected final Qualifier qualifier;

  public ProviderBeanFactory(Provider<T> provider, Scope scope, Qualifier qualifier) {
    this.provider = provider;
    this.scope = scope;
    this.qualifier = qualifier;
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
  public Class<T> getType() {
    return null;
  }

  @Override
  public Provider<T> getProvider(BeanRepository repo) {
    return provider;
  }

  @Override
  public Object getIdentifier() {
    return provider;
  }

}
