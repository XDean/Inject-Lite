package xdean.inject;

import static xdean.jex.util.lang.ExceptionUtil.uncheck;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Qualifier {
  public static final Qualifier EMPTY = new Qualifier(Collections.emptyList());

  public static Qualifier from(AnnotatedElement ae) {
    return new Qualifier(Arrays.stream(ae.getAnnotations())
        .filter(a -> a.getClass().getAnnotation(javax.inject.Qualifier.class) != null)
        .collect(Collectors.toList()));
  }

  public final List<Annotation> qualifiers;

  private Qualifier(List<Annotation> qualifiers) {
    this.qualifiers = Collections.unmodifiableList(qualifiers);
  }

  public boolean match(Qualifier other) {
    return other.qualifiers.stream().allMatch(a -> qualifiers.stream().anyMatch(anno -> equals(a, anno)));
  }

  private static boolean equals(Annotation a, Annotation b) {
    Class<? extends Annotation> type = a.annotationType();
    if (type != b.annotationType()) {
      return false;
    }
    return Arrays.stream(type.getMethods())
        .filter(m -> m.getDeclaringClass() == type)
        .filter(m -> m.getParameterCount() == 0)
        .allMatch(m -> uncheck(() -> m.invoke(a).equals(m.invoke(b))));
  }
}
