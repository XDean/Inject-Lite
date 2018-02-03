package xdean.inject.old;

import java.util.concurrent.Callable;

public class Assertion {

  static void assertNot(boolean b, String message) {
    assertThat(!b, message);
  }

  static void assertThat(boolean b, String message) {
    if (!b) {
      throw new IllegalDefineException(message);
    }
  }

  static <T> T assertNonNull(T t, String message) {
    assertThat(t != null, message);
    return t;
  }

  static <T> T assertTodo(Callable<T> c, String message) {
    try {
      return c.call();
    } catch (Exception e) {
      throw new IllegalDefineException(message, e);
    }
  }

}
