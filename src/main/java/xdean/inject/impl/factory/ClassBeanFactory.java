package xdean.inject.impl.factory;

import javax.inject.Provider;

import xdean.inject.BeanRepository;
import xdean.inject.IllegalDefineException;
import xdean.inject.Qualifier;
import xdean.inject.Scope;

public class ClassBeanFactory<T> extends AbstractAnnotationBeanFactory<Class<T>, T> {

  public ClassBeanFactory(Class<T> element, Scope scope, Qualifier qualifier) throws IllegalDefineException {
    super(element, scope, qualifier);
  }

  @Override
  public Class<T> getType() {
    return element;
  }

  @Override
  public Provider<T> getProvider(BeanRepository repo) {
    // TODO
    return null;
  }

}
