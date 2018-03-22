package xdean.inject.exception;

public class InjectException extends RuntimeException {
  public InjectException() {
    super();
  }

  public InjectException(String message, Throwable cause) {
    super(message, cause);
  }

  public InjectException(String message) {
    super(message);
  }

  public InjectException(Throwable cause) {
    super(cause);
  }
}
