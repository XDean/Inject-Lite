package xdean.inject.impl.factory;

import static xdean.jex.util.lang.ExceptionUtil.throwIt;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import javax.inject.Provider;

import xdean.inject.IllegalDefineException;
import xdean.jex.extra.collection.Pair;
import xdean.jex.util.reflect.TypeVisitor;

/**
 * Auto transform {@code Provider<Provider<T>>} to {@code Provider<T>}
 *
 * @author XDean
 */
@FunctionalInterface
@SuppressWarnings("rawtypes")
public interface ProviderTransformer<T> {

  ProviderTransformer FOR_INSTANCE = p -> p;
  ProviderTransformer FOR_PROVIDER = p -> (Provider<?>) p.get();

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
    return Pair.of(clz, provider ? FOR_PROVIDER : FOR_INSTANCE);
  }

  Provider<T> transform(Provider<?> p);
}
