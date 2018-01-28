package xdean.inject;

import java.util.Optional;

import javax.inject.Provider;

public interface BeanQuery<T> extends BeanConfig<T> {
  default Optional<T> get() {
    return getProvider().map(p -> p.get());
  }

  Optional<Provider<T>> getProvider();

  @Override
  BeanQuery<T> named(String name);

  @Override
  BeanQuery<T> qualifies(Qualifier<? super T> qualifier);

  @Override
  BeanQuery<T> scope(Scope scope);
}
