package xdean.inject.impl.factory;

import java.lang.reflect.AnnotatedElement;
import java.util.Objects;

import javax.inject.Provider;

import xdean.inject.BeanRepository;
import xdean.inject.Qualifier;
import xdean.inject.Scope;
import xdean.inject.exception.IllegalDefineException;
import xdean.inject.impl.BeanFactory;

public abstract class AbstractAnnotationBeanFactory<A extends AnnotatedElement, T> implements BeanFactory<T> {
  protected final A element;
  protected final Scope scope;
  protected final Qualifier qualifier;

  public AbstractAnnotationBeanFactory(A element, Scope scope, Qualifier qualifier)
      throws IllegalDefineException {
    this.element = element;
    this.scope = scope == Scope.UNDEFINED ? Scope.from(element) : scope;
    this.qualifier = Qualifier.from(element).and(qualifier);
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
  public Object getIdentifier() {
    return element;
  }

  @Override
  public final Provider<T> getProvider(BeanRepository repo) {
    Provider<T> p = getProviderActual(repo);
    return () -> {
      BeanFactoryContext.push(this);
      T v = p.get();
      BeanFactoryContext.pop(this);
      return v;
    };
  }

  protected abstract Provider<T> getProviderActual(BeanRepository repo);

  @Override
  public int hashCode() {
    return getIdentifier().hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof BeanFactory) {
      return Objects.equals(getIdentifier(), ((BeanFactory<?>) obj).getIdentifier());
    }
    return false;
  }
}
