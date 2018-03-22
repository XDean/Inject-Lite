package xdean.inject.old.model;

import static xdean.jex.util.lang.ExceptionUtil.uncheck;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import xdean.inject.old.InjectRepositoryImpl;
import xdean.inject.old.Util;
import xdean.jex.extra.collection.Pair;

public class MethodWrapper {
  private final Method method;
  private final List<Pair<Type, Qualifier>> qualifiers = new ArrayList<>();

  public MethodWrapper(Method Method) {
    this.method = Method;
    method.setAccessible(true);
    Parameter[] parameters = Method.getParameters();
    for (int i = 0; i < parameters.length; i++) {
      Parameter p = parameters[i];
      qualifiers.add(Pair.of(p.getParameterizedType(), Qualifier.from(p)));
    }
  }

  public void process(InjectRepositoryImpl repo, Object instance) {
    Object[] params = new Object[qualifiers.size()];
    for (int i = 0; i < params.length; i++) {
      Pair<Type, Qualifier> pair = qualifiers.get(i);
      params[i] = Util.get(repo, pair.getLeft(), pair.getRight());
    }
    uncheck(() -> method.invoke(instance, params));
  }
}
