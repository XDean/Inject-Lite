package xdean.inject;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * @param <T> the type of the input to the Qualifier
 * @since 0.1
 */
@FunctionalInterface
public interface Qualifier<T> extends Predicate<T> {

  @Override
  boolean test(T t);

  default Qualifier<T> and(Qualifier<? super T> other) {
    Objects.requireNonNull(other);
    return (t) -> test(t) && other.test(t);
  }

  default Qualifier<T> or(Qualifier<? super T> other) {
    Objects.requireNonNull(other);
    return (t) -> test(t) || other.test(t);
  }

  @Override
  default Qualifier<T> negate() {
    return (t) -> !test(t);
  }

  static <T> Qualifier<T> empty() {
    return e -> true;
  };

  static Qualifier<? extends AnnotatedElement> from(AnnotatedElement ae) {
    List<Annotation> qualifiers = Util.annotated(ae, javax.inject.Qualifier.class);
    if (qualifiers.isEmpty()) {
      return empty();
    } else {
      return other -> Util.annotated(other, javax.inject.Qualifier.class)
          .stream()
          .allMatch(a -> qualifiers.stream().anyMatch(anno -> a.equals(anno)));
    }
  }
}
