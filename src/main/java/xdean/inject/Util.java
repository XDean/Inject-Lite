package xdean.inject;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Util {

  static <T extends AnnotatedElement> List<T> annotated(T[] array, Class<? extends Annotation> annoType) {
    return Arrays.stream(array)
        .filter(t -> t.getAnnotation(annoType) != null)
        .collect(Collectors.toList());
  }

}
