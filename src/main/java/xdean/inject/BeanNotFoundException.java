package xdean.inject;

import java.util.function.Supplier;

public class BeanNotFoundException extends RuntimeException {

  public BeanNotFoundException(BeanRepository repo, Class<?> type) {
    this(repo, type, Qualifier.EMPTY);
  }

  public BeanNotFoundException(BeanRepository repo, Class<?> type, Qualifier qualifier) {
    super();
  }

  public static Supplier<BeanNotFoundException> notFound(BeanRepository repo, Class<?> type) {
    return notFound(repo, type, Qualifier.EMPTY);
  }

  public static Supplier<BeanNotFoundException> notFound(BeanRepository repo, Class<?> type, Qualifier qualifier) {
    return () -> new BeanNotFoundException(repo, type, qualifier);
  }
}
