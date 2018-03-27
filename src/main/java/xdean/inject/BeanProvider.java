package xdean.inject;

import java.util.function.Consumer;

import javax.inject.Provider;

import xdean.jex.util.function.EmptyFunction;

public interface BeanProvider<T> extends Provider<T> {
  T construct();

  void init(T t);

  @Override
  /* final */default T get() {
    T t = construct();
    init(t);
    return t;
  }

  static <T> BeanProvider<T> create(Provider<T> constructor, Consumer<T> initer) {
    return new BeanProvider<T>() {
      @Override
      public T construct() {
        return constructor.get();
      }

      @Override
      public void init(T t) {
        initer.accept(t);
      }
    };
  }

  static <T> BeanProvider<T> create(Provider<T> provider) {
    return create(provider, EmptyFunction.consumer());
  }
}
