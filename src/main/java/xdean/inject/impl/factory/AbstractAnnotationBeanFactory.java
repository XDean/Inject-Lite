package xdean.inject.impl.factory;

import java.lang.reflect.AnnotatedElement;

import xdean.inject.IllegalDefineException;
import xdean.inject.Qualifier;
import xdean.inject.Scope;
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
}
