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
public interface Qualifier extends Predicate<AnnotatedElement> {
  Qualifier EMPTY = create(e -> true, "empty");

  @Override
  boolean test(AnnotatedElement ae);

  String description();

  default Qualifier and(Qualifier other) {
    Objects.requireNonNull(other);
    return create((t) -> test(t) && other.test(t), String.format("(%s) AND (%s)", description(), other.description()));
  }

  default Qualifier or(Qualifier other) {
    Objects.requireNonNull(other);
    return create((t) -> test(t) || other.test(t), String.format("(%s) OR (%s)", description(), other.description()));
  }

  @Override
  default Qualifier negate() {
    return create((t) -> !test(t), "NOT " + description());
  }

  static Qualifier from(AnnotatedElement ae) {
    List<Annotation> qualifiers = Util.annotatedAnnotations(ae, javax.inject.Qualifier.class);
    if (qualifiers.isEmpty()) {
      return EMPTY;
    } else {
      return create(other -> Util.annotatedAnnotations(other, javax.inject.Qualifier.class)
          .stream()
          .allMatch(a -> qualifiers.stream().anyMatch(anno -> a.equals(anno))),
          "match " + ae);
    }
  }

  static Qualifier named(String name) {
    return create(other -> Optional.ofNullable(other.getAnnotation(Named.class)).map(n -> n.value().equals(name)).orElse(false),
        "named as " + name);
  }

  static Qualifier create(Predicate<AnnotatedElement> origin, String description) {
    return new Qualifier() {
      @Override
      public boolean test(AnnotatedElement ae) {
        return origin.test(ae);
      }

      @Override
      public String description() {
        return description;
      }

      @Override
      public String toString() {
        return "Qualifier(" + description + ")";
      }
    };
  }
}
