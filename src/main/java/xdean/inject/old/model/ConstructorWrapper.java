package xdean.inject.old.model;

import static xdean.jex.util.lang.ExceptionUtil.uncheck;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import xdean.inject.old.InjectRepositoryImpl;
import xdean.inject.old.Util;
import xdean.jex.extra.Pair;

public class ConstructorWrapper<T> {
  private final Constructor<T> constructor;
  private final List<Pair<Type, Qualifier>> qualifiers = new ArrayList<>();

  public ConstructorWrapper(Constructor<T> constructor) {
    this.constructor = constructor;
    Parameter[] parameters = constructor.getParameters();
    for (int i = 0; i < parameters.length; i++) {
      Parameter p = parameters[i];
      qualifiers.add(Pair.of(p.getParameterizedType(), Qualifier.from(p)));
    }
  }

  public T newInstance(InjectRepositoryImpl repo) {
    Object[] params = new Object[qualifiers.size()];
    for (int i = 0; i < params.length; i++) {
      Pair<Type, Qualifier> pair = qualifiers.get(i);
      params[i] = Util.get(repo, pair.getLeft(), pair.getRight());
    }
    return uncheck(() -> constructor.newInstance(params));
  }
}
