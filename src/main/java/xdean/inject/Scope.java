package xdean.inject;

import javax.inject.Provider;

public interface Scope {
  default boolean access(BeanCaller caller) {
    return true;
  }

  default <T> Provider<T> transform(Provider<T> provider) {
    return provider;
  }
}
