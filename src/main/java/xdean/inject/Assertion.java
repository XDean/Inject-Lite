package xdean.inject;

import java.util.concurrent.Callable;

public class Assertion {

  static void assertThat(boolean b, String message) {
    if (!b) {
      throw new IllegalDefineException(message);
    }
  }

  static <T> T assertTodo(Callable<T> c, String message) {
    try {
      return c.call();
    } catch (Exception e) {
      throw new IllegalDefineException(message, e);
    }
  }

}
