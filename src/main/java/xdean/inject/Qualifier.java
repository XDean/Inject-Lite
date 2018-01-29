package xdean.inject;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

import javax.inject.Named;

/**
 * @param the type of the input to the Qualifier
 * @since 0.1
 */
@FunctionalInterface
public interface Qualifier extends Predicate<AnnotatedElement> {
  Qualifier EMPTY = e -> true;

  @Override
  boolean test(AnnotatedElement ae);

  default Qualifier and(Qualifier other) {
    Objects.requireNonNull(other);
    return (t) -> test(t) && other.test(t);
  }

  default Qualifier or(Qualifier other) {
    Objects.requireNonNull(other);
    return (t) -> test(t) || other.test(t);
  }

  @Override
  default Qualifier negate() {
    return (t) -> !test(t);
  }

  static Qualifier from(AnnotatedElement ae) {
    List<Annotation> qualifiers = Util.annotatedAnnotations(ae, javax.inject.Qualifier.class);
    if (qualifiers.isEmpty()) {
      return EMPTY;
    } else {
      return other -> Util.annotatedAnnotations(other, javax.inject.Qualifier.class)
          .stream()
          .allMatch(a -> qualifiers.stream().anyMatch(anno -> a.equals(anno)));
    }
  }

  static Qualifier named(String name) {
    return other -> Optional.ofNullable(other.getAnnotation(Named.class)).map(n -> n.value().equals(name)).orElse(false);
  }
}
