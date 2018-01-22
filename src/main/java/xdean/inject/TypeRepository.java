package xdean.inject;

import static xdean.jex.util.lang.ExceptionUtil.uncatch;

import java.lang.annotation.Annotation;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import javax.inject.Provider;
import javax.inject.Scope;
import javax.inject.Singleton;

import xdean.inject.model.Qualifier;
import xdean.inject.model.QualifierWrapper;

class TypeRepository<T> {
  private List<QualifierWrapper<? extends T>> impls = new LinkedList<>();

  TypeRepository() {
  }

  <K extends T> void register(Class<K> impl) {
    impls.add(new QualifierWrapper<>(impl, getImplementation(impl)));
  }

  @SuppressWarnings("unchecked")
  Implementation<T> getImplementation(Class<? extends T> impl) {
    List<Annotation> scopes = Util.annotated(impl.getAnnotations(), Scope.class);
    Assertion.assertNot(scopes.size() > 1, "One class can only define one @Scope: " + impl);
    if (scopes.isEmpty()) {
      return new DefaultImpl<>(impl);
    }
    Annotation scope = scopes.get(0);
    if (scope.annotationType() == Singleton.class) {
      return new SingletonImpl<>(impl);
    }
    ScopeHandler handler = Assertion.assertNonNull(scope.annotationType().getAnnotation(ScopeHandler.class),
        "@Scope must also define @ScopeHandler: " + impl);
    return (Implementation<T>) Assertion.assertTodo(() -> uncatch(() -> handler.value().getConstructor(Class.class).newInstance(impl)),
        "ScopeHandler must define a public no-arg constructor: " + impl);
  }

  boolean hasImpl(Qualifier target) {
    return impls.stream().anyMatch(qw -> qw.match(target));
  }

  Optional<Provider<T>> getProvider(InjectRepository repo) {
    return getProvider(repo, Qualifier.EMPTY);
  }

  Optional<Provider<T>> getProvider(InjectRepository repo, Qualifier target) {
    if (hasImpl(target)) {
      return Optional.of(() -> get(repo, target).get());
    } else {
      return Optional.empty();
    }
  }

  Optional<? extends T> get(InjectRepository repo) {
    return get(repo, Qualifier.EMPTY);
  }

  Optional<? extends T> get(InjectRepository repo, Qualifier target) {
    return impls.stream()
        .map(qw -> qw.get(repo, target))
        .filter(Optional::isPresent)
        .map(Optional::get)
        .findFirst();
  }
}