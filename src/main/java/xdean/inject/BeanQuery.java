package xdean.inject;

import java.util.Optional;

import javax.inject.Provider;

public interface BeanQuery<T> {
  default Optional<T> get() {
    return getProvider().map(p -> p.get());
  }

  Optional<Provider<T>> getProvider();

  default BeanQuery<T> named(String name) {
    return qualifies(Qualifier.named(name));
  }

  BeanQuery<T> qualifies(Qualifier qualifier);
}
