package xdean.inject.impl;

import java.util.Optional;

import javax.inject.Provider;

import xdean.inject.api.BeanRepository;

public class BeanRepositoryImpl implements BeanRepository {

  @Override
  public <T> void scan(Class<T> clz) {

  }

  @Override
  public <T> void register(Class<T> beanClz, Class<? extends T> impl) {

  }

  @Override
  public <T> void register(Class<T> beanClz, Provider<? extends T> impl) {

  }

  @Override
  public <T> Optional<T> getBean(Class<T> clz) {
    return null;
  }

  @Override
  public <T> Optional<T> getBean(Class<T> clz, String name) {
    return null;
  }
}
