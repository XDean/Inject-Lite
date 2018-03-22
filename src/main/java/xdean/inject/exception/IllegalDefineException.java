package xdean.inject.exception;

import java.util.concurrent.Callable;

public class IllegalDefineException extends RuntimeException {

  public IllegalDefineException() {
    super();
  }

  public IllegalDefineException(String message, Throwable cause) {
    super(message, cause);
  }

  public IllegalDefineException(String message) {
    super(message);
  }

  public IllegalDefineException(Throwable cause) {
    super(cause);
  }

  public static void assertNot(boolean b, String message) {
    assertThat(!b, message);
  }

  public static void assertThat(boolean b, String message) {
    if (!b) {
      throw new IllegalDefineException(message);
    }
  }

  public static <T> T assertNot(T t, boolean b, String message) {
    return assertThat(t, !b, message);
  }

  public static <T> T assertThat(T t, boolean b, String message) {
    assertThat(b, message);
    return t;
  }

  public static <T> T assertNonNull(T t, String message) {
    assertThat(t != null, message);
    return t;
  }

  public static <T> T assertTodo(Callable<T> c, String message) {
    try {
      return c.call();
    } catch (Exception e) {
      throw new IllegalDefineException(message, e);
    }
  }
}
