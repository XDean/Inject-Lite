package xdean.inject.impl;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import javax.inject.Provider;

import xdean.inject.BeanQuery;
import xdean.inject.BeanRegister;
import xdean.inject.BeanRepository;
import xdean.inject.Qualifier;
import xdean.inject.Scope;

public class BeanRepositoryImpl implements BeanRepository {

  @Override
  public <T> BeanRegister<T> register() {
    return new Register<>();
  }

  @Override
  public <T> BeanQuery<T> query(Class<T> beanClass) {
    return null;
  }

  @Override
  public <T> void scan(Class<T> clz) {

  }

  private static class Register<T> implements BeanRegister<T> {

    @Override
    public boolean from(Class<? extends T> clz) {
      return false;
    }

    @Override
    public boolean from(Field field) {
      return false;
    }

    @Override
    public boolean from(Method method) {
      return false;
    }

    @Override
    public boolean from(Provider<? extends T> provider) {
      return false;
    }

    @Override
    public BeanRegister<T> implementsFor(Class<? super T> clz) {
      return null;
    }

    @Override
    public BeanRegister<T> named(String name) {
      return null;
    }

    @Override
    public BeanRegister<T> qualifies(Qualifier<? super T> qualifier) {
      return null;
    }

    @Override
    public BeanRegister<T> scope(Scope scope) {
      return null;
    }
  }
}
