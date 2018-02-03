package xdean.inject.old;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Provider;

import xdean.inject.old.model.Qualifier;

public class Util {

  public static <T extends AnnotatedElement> List<T> annotated(T[] array, Class<? extends Annotation> annoType) {
    return Arrays.stream(array)
        .filter(t -> t.getAnnotation(annoType) != null)
        .collect(Collectors.toList());
  }

  /**
   * Get annotations whose class declared with specific Annotation from the
   * {@link AnnotatedElement}.
   */
  public static List<Annotation> annotated(AnnotatedElement ae, Class<? extends Annotation> annotationOnAnnotationType) {
    return Arrays.stream(ae.getAnnotations())
        .filter(t -> t.annotationType().getAnnotation(annotationOnAnnotationType) != null)
        .collect(Collectors.toList());
  }

  public static <T> Object get(InjectRepositoryImpl repo, Type type) {
    return get(repo, type, Qualifier.EMPTY);
  }

  @SuppressWarnings("unchecked")
  public static <T> Object get(InjectRepositoryImpl repo, Type type, Qualifier qualifier) {
    if (type instanceof ParameterizedType) {
      ParameterizedType pt = (ParameterizedType) type;
      if (pt.getRawType() == Provider.class) {
        return repo.getProvider((Class<T>) pt.getActualTypeArguments()[0], qualifier);
      } else {
        type = pt.getRawType();
      }
    } else if (type instanceof TypeVariable) {
      type = ((TypeVariable<?>) type).getBounds()[0];
    }
    if (type instanceof Class) {
      return repo.get((Class<T>) type, qualifier);
    }
    throw new IllegalArgumentException();
  }
}
