package xdean.inject;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import xdean.inject.model.ConstructorWrapper;
import xdean.inject.model.FieldWrapper;

class DefaultImpl<T> implements Implementation<T> {
  final Class<? extends T> type;
  final ConstructorWrapper<? extends T> constructor;
  final List<Method> methods;
  final List<FieldWrapper> fields;

  DefaultImpl(Class<? extends T> type) {
    this.type = type;
    this.constructor = new ConstructorWrapper<>(initConstructor());
    this.methods = initMethods();
    this.fields = initFields().stream().map(FieldWrapper::new).collect(Collectors.toList());
  }

  @SuppressWarnings("unchecked")
  private Constructor<? extends T> initConstructor() {
    List<Constructor<?>> injectConstructors = Util.annotated(type.getDeclaredConstructors(), Inject.class);
    Assertion.assertThat(injectConstructors.size() < 2, "At most one constructor can have @Inject: " + type);
    if (injectConstructors.isEmpty()) {
      return Assertion.assertTodo(type::getConstructor, "Public no-arg constructor should be defined if no @Inject constructor: " + type);
    } else {
      return (Constructor<? extends T>) injectConstructors.get(0);
    }
  }

  private List<Method> initMethods() {
    List<Method> methods = Util.annotated(type.getDeclaredMethods(), Inject.class);
    for (Method method : methods) {
      Assertion.assertNot(Modifier.isStatic(method.getModifiers()), "Not support inject static method: " + method);
    }
    return methods;
  }

  private List<Field> initFields() {
    List<Field> fields = Util.annotated(type.getDeclaredFields(), Inject.class);
    for (Field field : fields) {
      Assertion.assertNot(Modifier.isStatic(field.getModifiers()), "Not support inject static field: " + field);
      Assertion.assertNot(Modifier.isFinal(field.getModifiers()), "Can't inject final field: " + field);
    }
    return fields;
  }

  @Override
  public T get(InjectRepository repo) {
    T instance = constructor.newInstance(repo);
    fields.forEach(fw->fw.process(repo, instance));
    return instance;
  }
}