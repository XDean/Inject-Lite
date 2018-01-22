package xdean.inject;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

class ImplementationImpl<T> implements Implementation {
  final TypeRepository<T> typeRepo;
  final Constructor<? extends T> constructor;
  final List<Annotation> qualifier = new LinkedList<>();
  Annotation scope;

  ImplementationImpl(TypeRepository<T> typeRepository, Class<? extends T> impl) {
    typeRepo = typeRepository;
    constructor = initConstructor(impl);
  }

  @SuppressWarnings("unchecked")
  private Constructor<? extends T> initConstructor(Class<? extends T> impl) {
    List<Constructor<?>> injectConstructors = Util.annotated(impl.getDeclaredConstructors(), Inject.class);
    Assertion.assertThat(injectConstructors.size() > 1, "At most one constructor can have @Inject");
    if (injectConstructors.isEmpty()) {
      return Assertion.assertTodo(impl::getConstructor, "Public no-arg constructor should be defined if no @Inject constructor.");
    } else {
      return (Constructor<? extends T>) injectConstructors.get(0);
    }
  }

  T create() {
    return null;
  }
}