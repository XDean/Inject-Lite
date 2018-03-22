package xdean.inject.impl.factory;

import static xdean.jex.util.lang.ExceptionUtil.throwIt;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import javax.inject.Provider;

import xdean.inject.IllegalDefineException;
import xdean.jex.extra.Pair;
import xdean.jex.util.reflect.TypeVisitor;

/**
 * Auto transform {@code Provider<Provider<T>>} to {@code Provider<T>}
 *
 * @author XDean
 */
@FunctionalInterface
public interface ProviderTransformer<T> {

  @SuppressWarnings("unchecked")
  static <T> Pair<Class<T>, ProviderTransformer<T>> from(Type declaredType, Class<? super T> target, Object definedObject) {
    Type actualType;
    boolean provider = false;
    if (declaredType instanceof ParameterizedType) {
      ParameterizedType pt = (ParameterizedType) declaredType;
      Type rawType = pt.getRawType();
      if (rawType == Provider.class) {
        provider = true;
        actualType = pt.getActualTypeArguments()[0];
      } else {
        actualType = pt;
      }
    } else {
      actualType = declaredType;
    }
    Class<T> clz = (Class<T>) TypeVisitor.<Class<?>> create(actualType)
        .onClass(c -> c)
        .onParameterizedType(pt -> (Class<?>) pt.getRawType())
        .result(() -> throwIt(new IllegalDefineException("Bean for T must defined as T or Provider<T>: " + definedObject)));
    IllegalDefineException.assertThat(target.isAssignableFrom(clz),
        "Can't convert from " + clz + " to " + target + ": " + definedObject);
    return Pair.of(clz, provider ? p -> (Provider<T>) p.get() : p -> (Provider<T>) p);
  }

  Provider<T> transform(Provider<?> p);
}
