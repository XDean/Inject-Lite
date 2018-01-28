package xdean.inject;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import javax.inject.Provider;

public interface BeanRegister<T> extends BeanConfig<T> {

  boolean from(Class<? extends T> clz);

  boolean from(Field field);

  boolean from(Method method);

  boolean from(Provider<? extends T> provider);

  BeanRegister<T> implementsFor(Class<? super T> clz);

  @Override
  BeanRegister<T> named(String name);

  @Override
  BeanRegister<T> qualifies(Qualifier<? super T> qualifier);

  @Override
  BeanRegister<T> scope(Scope scope);
}
