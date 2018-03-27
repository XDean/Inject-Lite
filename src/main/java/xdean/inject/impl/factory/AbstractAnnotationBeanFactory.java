package xdean.inject.impl.factory;

import java.lang.reflect.AnnotatedElement;
import java.util.Objects;

import xdean.inject.BeanProvider;
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
  public final BeanProvider<T> getProvider(BeanRepository repo) {
    BeanProvider<T> p = getProviderActual(repo);
    return BeanProvider.create(() -> {
      T t = BeanFactoryContext.push(this, null);
      if (t == null) {
        t = p.construct();
        BeanFactoryContext.pop(this);
        BeanFactoryContext.push(this, t);
        p.init(t);
      }
      BeanFactoryContext.pop(this);
      return t;
    });
  }

  protected abstract BeanProvider<T> getProviderActual(BeanRepository repo);

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
