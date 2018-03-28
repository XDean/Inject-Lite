package xdean.inject.impl;

import static xdean.inject.exception.BeanNotFoundException.notFound;

import java.lang.reflect.Executable;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import xdean.inject.BeanRepository;
import xdean.inject.Qualifier;

public class Util {

  public static Error never(Throwable e) {
    return new Error("This never happen. Contact author.", e);
  }

  public static Object[] prepareArgument(Executable element, BeanRepository repo) {
    Parameter[] parameters = element.getParameters();
    Object[] params = new Object[element.getParameterCount()];
    for (int i = 0; i < params.length; i++) {
      Parameter p = parameters[i];
      Qualifier q = Qualifier.from(p);
      Object o = repo.query(p.getType()).qualifies(q).get().orElseThrow(notFound(repo, p.getType(), q));
      params[i] = o;
    }
    return params;
  }

  /**
   * Get all top instance methods. Following methods are ignored
   * <ul>
   * <li>Overridden method</li>
   * <li>Bridge method</li>
   * <li>Synthetic method</li>
   * </ul>
   */
  public static List<Method> getTopMethods(Class<?> clz) {
    List<Method> result = new ArrayList<>();
    Class<?> c = clz;
    do {
      result.addAll(Arrays.stream(c.getDeclaredMethods())
          .filter(m -> !(m.isSynthetic() || m.isBridge()))
          .filter(m -> !result.stream()
              .filter(o -> Objects.equals(m.getName(), o.getName()))
              .filter(o -> o.getParameterCount() == m.getParameterCount())
              .filter(o -> Arrays.equals(o.getParameterTypes(), m.getParameterTypes()))
              .findAny()
              .isPresent())
          .collect(Collectors.toList()));
    } while ((c = c.getSuperclass()) != null);
    return result;
  }
}
