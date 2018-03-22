package xdean.inject.impl;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.WeakHashMap;

import javax.inject.Provider;

import xdean.inject.BeanQuery;
import xdean.inject.BeanRegister;
import xdean.inject.BeanRepository;
import xdean.inject.Qualifier;
import xdean.inject.Scope;
import xdean.inject.annotation.Bean;
import xdean.inject.exception.IllegalDefineException;
import xdean.inject.impl.factory.ClassBeanFactory;
import xdean.inject.impl.factory.FieldBeanFactory;
import xdean.inject.impl.factory.MethodBeanFactory;

public class BeanRepositoryImpl implements BeanRepository {

  @SuppressWarnings({ "unchecked", "rawtypes" })
  private class BeanFactories {
    Map<Class/* <T> */, List/* <BeanFactory<? extends T>> */> factories = new WeakHashMap<>();
    Map<BeanFactory/* <T> */, Provider/* <T> */> providers = new WeakHashMap<>();

    <T> List<BeanFactory<? extends T>> getFactory(Class<T> beanClass) {
      return Collections.unmodifiableList(factories.getOrDefault(beanClass, Collections.emptyList()));
    }

    <T> void addFactory(Class<T> beanClass, BeanFactory<? extends T> factory) {
      factories.computeIfAbsent(beanClass, k -> new LinkedList<>()).add(factory);
    }

    <T> Provider<T> getProvider(BeanFactory<T> factory) {
      return providers.computeIfAbsent(factory, f -> f.getProvider(BeanRepositoryImpl.this));
    }
  }

  private final BeanFactories factories = new BeanFactories();

  @Override
  public <T> BeanRegister<T> register() {
    return new Register<>();
  }

  @Override
  public <T> BeanQuery<T> query(Class<T> beanClass) {
    return new Query<>(beanClass);
  }

  @Override
  public <T> void scan(Class<T> clz) {

  }

  private class Register<T> implements BeanRegister<T> {
    private final Set<Class<? super T>> beanClasses = new HashSet<>();
    private Qualifier qualifier = Qualifier.EMPTY;
    private Scope scope = Scope.UNDEFINED;

    private void addToRepository(BeanFactory<T> factory) {
      (beanClasses.isEmpty() ? Arrays.asList(factory.getType()) : beanClasses)
          .forEach(c -> factories.addFactory(c, factory.validateImplements(c)));
    }

    @SuppressWarnings("unchecked")
    private void autoImpl(BeanFactory<T> factory, AnnotatedElement ae) {
      Bean anno = ae.getAnnotation(Bean.class);
      if (anno == null) {
        return;
      }
      Set<Class<?>> set = new HashSet<>();
      Collections.addAll(set, anno.value());
      if (anno.implSuperClass()) {
        set.add(factory.getType().getSuperclass());
      }
      if (anno.implAllInterfaces()) {
        Collections.addAll(set, factory.getType().getInterfaces());
      }
      set.forEach(c -> {
        factory.validateImplements(c);
        implementsFor((Class<? super T>) c);
      });
    }

    @Override
    public void from(Class<T> clz) throws IllegalDefineException {
      BeanFactory<T> factory = new ClassBeanFactory<>(clz, scope, qualifier);
      autoImpl(factory, clz);
      addToRepository(factory);
    }

    @Override
    public void from(Field field) throws IllegalDefineException {
      BeanFactory<T> factory = new FieldBeanFactory<>(field, scope, qualifier);
      autoImpl(factory, field);
      addToRepository(factory);
    }

    @Override
    public void from(Method method) throws IllegalDefineException {
      BeanFactory<T> factory = new MethodBeanFactory<>(method, scope, qualifier);
      autoImpl(factory, method);
      addToRepository(factory);
    }

    @Override
    public void from(Provider<T> provider) throws IllegalDefineException {
      throw new UnsupportedOperationException();
    }

    @Override
    public BeanRegister<T> implementsFor(Class<? super T> clz) {
      beanClasses.add(clz);
      return this;
    }

    @Override
    public BeanRegister<T> qualifier(Qualifier qualifier) {
      this.qualifier = this.qualifier.and(qualifier);
      return this;
    }

    @Override
    public BeanRegister<T> scope(Scope scope) {
      this.scope = scope;
      return this;
    }
  }

  private class Query<T> implements BeanQuery<T> {
    private Qualifier qualifier = Qualifier.EMPTY;
    private final Class<T> clz;

    public Query(Class<T> clz) {
      this.clz = clz;
    }

    @Override
    public Optional<Provider<? extends T>> getProvider() {
      return factories.getFactory(clz)
          .stream()
          .filter(f -> f.getQualifier().match(qualifier))
          .findFirst()
          .<Provider<? extends T>> map(f -> factories.getProvider(f));
    }

    @Override
    public BeanQuery<T> qualifies(Qualifier qualifier) {
      this.qualifier = this.qualifier.and(qualifier);
      return this;
    }
  }
}
