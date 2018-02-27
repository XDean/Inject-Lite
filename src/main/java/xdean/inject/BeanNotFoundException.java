package xdean.inject;

public class BeanNotFoundException extends RuntimeException {

  public BeanNotFoundException(BeanRepository repo, Class<?> type) {
    this(repo, type, Qualifier.EMPTY);
  }

  public BeanNotFoundException(BeanRepository repo, Class<?> type, Qualifier qualifier) {
    super();
  }
}
