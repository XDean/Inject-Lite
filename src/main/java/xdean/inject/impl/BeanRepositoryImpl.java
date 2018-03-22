package xdean.inject.impl;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.WeakHashMap;

import javax.inject.Provider;

import xdean.inject.BeanQuery;
import xdean.inject.BeanRegister;
import xdean.inject.BeanRepository;
import xdean.inject.IllegalDefineException;
import xdean.inject.Qualifier;
import xdean.inject.Scope;
import xdean.inject.impl.factory.ClassBeanFactory;
import xdean.inject.impl.factory.FieldBeanFactory;
import xdean.inject.impl.factory.MethodBeanFactory;

public class BeanRepositoryImpl implements BeanRepository {

  @SuppressWarnings({ "unchecked", "rawtypes" })
  private static class BeanFactories {
    Map<Class/* <T> */, List/* <BeanFactory<? extends T>> */> data = new WeakHashMap<>();

    <T> List<BeanFactory<? extends T>> get(Class<T> beanClass) {
      return Collections.unmodifiableList(data.getOrDefault(beanClass, Collections.emptyList()));
    }

    <T> void add(Class<T> beanClass, BeanFactory<? extends T> factory) {
      data.computeIfAbsent(beanClass, k -> new LinkedList<>()).add(factory);
    }
  }

  private final BeanFactories factories = new BeanFactories();

  @Override
  public <T> BeanRegister<T> register() {
    return new Register<>();
  }

  @Override
  public <T> BeanQuery<T> query(Class<T> beanClass) {
    return new Query<>();
  }

  @Override
  public <T> void scan(Class<T> clz) {

  }

  private class Register<T> implements BeanRegister<T> {
    private final List<Class<? super T>> beanClasses = new ArrayList<>();
    private Qualifier qualifier = Qualifier.EMPTY;
    private Scope scope = Scope.UNDEFINED;

    private void addToRepository(BeanFactory<T> factory) {
      (beanClasses.isEmpty() ? Arrays.asList(factory.getType()) : beanClasses)
          .forEach(c -> factories.add(c, factory.validateImplements(c)));
    }

    @Override
    public void from(Class<T> clz) throws IllegalDefineException {
      addToRepository(new ClassBeanFactory<>(clz, scope, qualifier));
    }

    @Override
    public void from(Field field) throws IllegalDefineException {
      addToRepository(new FieldBeanFactory<>(field, scope, qualifier));
    }

    @Override
    public void from(Method method) throws IllegalDefineException {
      addToRepository(new MethodBeanFactory<>(method, scope, qualifier));
    }

    @Override
    public void from(Provider<T> provider) throws IllegalDefineException {
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
    @Override
    public Optional<Provider<T>> getProvider() {
      return null;
    }

    @Override
    public BeanQuery<T> named(String name) {
      return null;
    }

    @Override
    public BeanQuery<T> qualifies(Qualifier qualifier) {
      return null;
    }
  }
}
