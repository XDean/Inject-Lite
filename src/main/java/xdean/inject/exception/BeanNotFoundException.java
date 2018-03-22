package xdean.inject.exception;

import java.util.function.Supplier;

import xdean.inject.BeanRepository;
import xdean.inject.Qualifier;

public class BeanNotFoundException extends RuntimeException {

  public static Supplier<BeanNotFoundException> notFound(BeanRepository repo, Class<?> type) {
    return notFound(repo, type, Qualifier.EMPTY);
  }

  public static Supplier<BeanNotFoundException> notFound(BeanRepository repo, Class<?> type, Qualifier qualifier) {
    return () -> new BeanNotFoundException(repo, type, qualifier);
  }

  public BeanNotFoundException(BeanRepository repo, Class<?> type) {
    this(repo, type, Qualifier.EMPTY);
  }

  public BeanNotFoundException(BeanRepository repo, Class<?> type, Qualifier qualifier) {
    super(format(repo, type, qualifier));
  }

  private static String format(BeanRepository repo, Class<?> type, Qualifier qualifier) {
    return String.format("Can't find %s bean in %s with qualifier()", type, repo, qualifier);
  }
}
