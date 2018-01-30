package xdean.inject.impl.factory;

import java.lang.reflect.Field;

import javax.inject.Provider;

import xdean.inject.IllegalDefineException;
import xdean.inject.Qualifier;
import xdean.inject.Scope;

public class FieldBeanFactory<T> extends AbstractAnnotationBeanFactory<Field, T> {

  public FieldBeanFactory(Field element, Scope scope, Qualifier qualifier) throws IllegalDefineException {
    super(element, scope, qualifier);
  }

  @Override
  public Class<T> getType() {
    return null;
  }

  @Override
  public Provider<T> getProvider() {
    // TODO Auto-generated method stub
    return null;
  }

}
