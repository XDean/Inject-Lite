package xdean.inject;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import javax.inject.Provider;
import javax.inject.Singleton;

import xdean.inject.annotation.Scan;
import xdean.inject.annotation.ScopeHandler;
import xdean.inject.exception.IllegalDefineException;
import xdean.jex.extra.tryto.Try;

public interface Scope {

  Scope UNDEFINED = new Scope() {
  };
  Scope DEFAULT = new Scope() {
  };
  Scope SINGLETON = new Scope() {
    @Override
    public <T> BeanProvider<T> transform(BeanProvider<T> provider) {
      return new BeanProvider<T>() {
        T t = null;
        AtomicBoolean init = new AtomicBoolean(false);

        @Override
        public T construct() {
          if (t == null) {
            synchronized (this) {
              if (t == null) {
                t = provider.construct();
              }
            }
          }
          return t;
        }

        @Override
        public void init(T t) {
          if (init.compareAndSet(false, true)) {
            provider.init(t);
          }
        }
      };
    }
  };

  default boolean access(BeanCaller caller) {
    return true;
  }

  default <T> BeanProvider<T> transform(BeanProvider<T> provider) {
    return provider;
  }

  default <T> BeanProvider<T> transform(Provider<T> constructor, Consumer<T> initer) {
    return transform(BeanProvider.create(constructor, initer));
  }

  static Scope from(AnnotatedElement ae) throws IllegalDefineException {
    List<Annotation> scopes = Util.annotatedAnnotations(ae, javax.inject.Scope.class);
    IllegalDefineException.assertThat(scopes.size() < 2, "Can't define more than 1 scope: " + ae);
    if (scopes.isEmpty()) {
      return DEFAULT;
    }
    Annotation scopeAnno = scopes.get(0);
    Class<? extends Annotation> annoType = scopeAnno.annotationType();
    if (annoType == Singleton.class || annoType == Scan.class) {
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
