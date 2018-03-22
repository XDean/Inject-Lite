package xdean.inject;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.inject.Named;

import com.google.common.collect.ImmutableSet;

import xdean.jex.util.reflect.AnnotationUtil;

/**
 * @param the type of the input to the Qualifier
 * @since 0.1
 */
public interface Qualifier {
  Qualifier EMPTY = create(Collections.emptySet(), "empty");

  Collection<? extends Annotation> annotations();

  String description();

  default boolean match(Qualifier other) {
    return annotations().containsAll(other.annotations());
  }

  default Qualifier and(Qualifier other) {
    Objects.requireNonNull(other);
    return create(ImmutableSet.<Annotation> builder()
        .addAll(annotations())
        .addAll(other.annotations())
        .build(), String.format("%s AND %s", description(), other.description()));
  }

  static Qualifier from(AnnotatedElement ae) {
    List<Annotation> qualifiers = Util.annotatedAnnotations(ae, javax.inject.Qualifier.class);
    if (qualifiers.isEmpty()) {
      return EMPTY;
    } else {
      return create(qualifiers, "match " + ae);
    }
  }

  static Qualifier named(String name) {
    return create(Collections.singleton(
        AnnotationUtil.createAnnotationFromMap(Named.class, Collections.singletonMap("value", name))),
        "named as " + name);
  }

  static Qualifier create(Collection<? extends Annotation> annotations, String description) {
    ImmutableSet<? extends Annotation> copy = ImmutableSet.copyOf(annotations);
    return new Qualifier() {
      @Override
      public Collection<? extends Annotation> annotations() {
        return copy;
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
