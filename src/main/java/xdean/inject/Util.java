package xdean.inject;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

class Util {

  public static <T extends AnnotatedElement> List<T> annotated(T[] array, Class<? extends Annotation> annoType) {
    return Arrays.stream(array)
        .filter(t -> t.getAnnotation(annoType) != null)
        .collect(Collectors.toList());
  }

  /**
   * Get annotations whose class declared with specific Annotation from the
   * {@link AnnotatedElement}.
   */
  public static List<Annotation> annotatedAnnotations(AnnotatedElement ae, Class<? extends Annotation> annotationOnAnnotationType) {
    return Arrays.stream(ae.getAnnotations())
        .filter(t -> t.annotationType().getAnnotation(annotationOnAnnotationType) != null)
        .collect(Collectors.toList());
  }
}
