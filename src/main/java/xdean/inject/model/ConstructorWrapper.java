package xdean.inject.model;

import static xdean.jex.util.lang.ExceptionUtil.uncheck;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;

import xdean.inject.InjectRepository;
import xdean.inject.Util;

public class ConstructorWrapper<T> {
  private final Constructor<T> constructor;

  public ConstructorWrapper(Constructor<T> constructor) {
    this.constructor = constructor;
  }

  public T newInstance(InjectRepository repo) {
    Parameter[] parameters = constructor.getParameters();
    Object[] params = new Object[parameters.length];
    for (int i = 0; i < params.length; i++) {
      Parameter p = parameters[i];
      params[i] = Util.get(repo, p.getParameterizedType(), Qualifier.from(p));
    }
    return uncheck(() -> constructor.newInstance(params));
  }
}
