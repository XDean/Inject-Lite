package xdean.inject.model;

import static xdean.jex.util.lang.ExceptionUtil.uncheck;

import java.lang.reflect.Field;

import xdean.inject.InjectRepository;

public class FieldWrapper {
  private final Field field;
  private Qualifier qualifier;

  public FieldWrapper(Field field) {
    this.field = field;
    field.setAccessible(true);
    this.qualifier = Qualifier.from(field);
  }

  public void process(InjectRepository repo, Object o) {
    Object value = repo.get(field.getType(), qualifier);
    uncheck(() -> field.set(o, value));
  }
}
