package xdean.inject.impl.old.model;

import static xdean.jex.util.lang.ExceptionUtil.uncheck;

import java.lang.reflect.Field;

import xdean.inject.impl.old.InjectRepositoryImpl;
import xdean.inject.impl.old.Util;

public class FieldWrapper {
  private final Field field;
  private Qualifier qualifier;

  public FieldWrapper(Field field) {
    this.field = field;
    field.setAccessible(true);
    this.qualifier = Qualifier.from(field);
  }

  public void process(InjectRepositoryImpl repo, Object o) {
    Object value = Util.get(repo, field.getGenericType(), qualifier);
    uncheck(() -> field.set(o, value));
  }
}
