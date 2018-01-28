package xdean.inject;

public interface BeanConfig<T> {
  BeanConfig<T> named(String name);

  BeanConfig<T> qualifies(Qualifier<? super T> qualifier);

  BeanConfig<T> scope(Scope scope);
}
