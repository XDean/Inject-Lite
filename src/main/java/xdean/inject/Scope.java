package xdean.inject;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.List;

import javax.inject.Provider;
import javax.inject.Singleton;

import xdean.inject.annotation.ScopeHandler;
import xdean.jex.extra.LazyValue;
import xdean.jex.util.task.tryto.Try;

public interface Scope {

  Scope DEFAULT = new Scope() {
  };
  Scope SINGLETON = new Scope() {
    @Override
    public <T> Provider<T> transform(Provider<T> provider) {
      return LazyValue.create(provider::get)::get;
    }
  };

  default boolean access(BeanCaller caller) {
    return true;
  }

  default <T> Provider<T> transform(Provider<T> provider) {
    return provider;
  }

  static Scope from(AnnotatedElement ae) throws IllegalDefineException {
    List<Annotation> scopes = Util.annotatedAnnotations(ae, javax.inject.Scope.class);
    IllegalDefineException.assertThat(scopes.size() < 2, "Can't define more than 1 scope: " + ae);
    if (scopes.isEmpty()) {
      return DEFAULT;
    }
    Annotation scopeAnno = scopes.get(0);
    Class<? extends Annotation> annoType = scopeAnno.annotationType();
    if (annoType == Singleton.class) {
      return SINGLETON;
    }
    ScopeHandler scopeHandler = IllegalDefineException.assertNonNull(annoType.getAnnotation(ScopeHandler.class),
        "@Scope annotation must define @ScopeHandler: " + annoType);
    Class<? extends Scope> scopeClass = scopeHandler.value();
    return Try.<Scope> to(() -> scopeClass.getConstructor().newInstance())
        .recoverWith(e -> Try.to(() -> scopeClass.getConstructor(Annotation.class).newInstance(scopeAnno)))
        .toOptional()
        .orElseThrow(() -> new IllegalDefineException(
            "Scope must define a public no-arg constructor or public constructor with one paramter Annotation: " + scopeClass));
  }
}
