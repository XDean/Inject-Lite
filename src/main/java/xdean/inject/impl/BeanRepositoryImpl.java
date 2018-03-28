package xdean.inject.impl;

import static xdean.jex.util.lang.ExceptionUtil.uncatch;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.function.Consumer;

import javax.inject.Provider;

import io.reactivex.Flowable;
import xdean.inject.BeanQuery;
import xdean.inject.BeanRegister;
import xdean.inject.BeanRepository;
import xdean.inject.BeanRepositoryConfig;
import xdean.inject.Qualifier;
import xdean.inject.Scope;
import xdean.inject.annotation.Bean;
import xdean.inject.annotation.Scan;
import xdean.inject.exception.IllegalDefineException;
import xdean.inject.impl.factory.ClassBeanFactory;
import xdean.inject.impl.factory.FieldBeanFactory;
import xdean.inject.impl.factory.MethodBeanFactory;
import xdean.jex.log.Logable;
import xdean.jex.util.reflect.ReflectUtil;

public class BeanRepositoryImpl implements BeanRepository, Logable {

  @SuppressWarnings({ "unchecked", "rawtypes" })
  private class BeanFactories {
    Map<Class/* <T> */, Set/* <BeanFactory<? extends T>> */> factoryMap = new WeakHashMap<>();
    Map<BeanFactory/* <T> */, Provider/* <T> */> providers = new WeakHashMap<>();

    <T> Collection<BeanFactory<T>> getFactory(Class<T> beanClass) {
      return Collections.unmodifiableCollection(factoryMap.getOrDefault(beanClass, Collections.emptySet()));
    }

    <T> void addFactory(Class<T> beanClass, BeanFactory<? extends T> factory) {
      factoryMap.computeIfAbsent(beanClass, k -> new LinkedHashSet<>()).add(factory);
    }

    <T> Provider<T> getProvider(BeanFactory<T> factory) {
      return providers.computeIfAbsent(factory, f -> f.getProvider(BeanRepositoryImpl.this));
    }
  }

  private final BeanRepositoryConfigImpl config = new BeanRepositoryConfigImpl();
  private final Map<Class<?>, Object> scaned = new WeakHashMap<>();
  private final BeanFactories factories = new BeanFactories();

  @Override
  public BeanRepository config(Class<?> config) {
    debug("Read config from: " + config);
    // TODO
    return this;
  }

  @Override
  public BeanRepository config(Consumer<BeanRepositoryConfig> config) {
    config.accept(this.config);
    return this;
  }

  @Override
  public <T> BeanRegister<T> register() {
    return new Register<>();
  }

  @Override
  public <T> BeanQuery<T> query(Class<T> beanClass) {
    if (config.autoRegister && (scaned.put(beanClass, this) == null)) {
      uncatch(() -> scan(beanClass));
    }
    return new Query<>(beanClass);
  }

  @Override
  public BeanRepository scan(Class<?> clz) {
    debug("Scan beans from Class: " + clz);
    scanBean(clz);
    return this;
  }

  @Override
  public BeanRepository scan(boolean inherit, String... packages) {
    debug("Scan beans from Packages: " + Arrays.toString(packages));
    Flowable.fromArray(packages)
        .flatMap(p -> Flowable.fromIterable(config.classpaths).flatMap(cp -> cp.scan(p, inherit)))
        .forEach(c -> scanBean(c));
    return this;
  }

  private <T> void scanBean(Class<T> clz) {
    if (scaned.put(clz, this) != null) {
      return;
    }
    uncatch(() -> register(clz));
    Scan scan = clz.getAnnotation(Scan.class);
    if (scan != null) {
      Arrays.stream(ReflectUtil.getAllFields(clz, true))
          .filter(f -> f.isAnnotationPresent(Bean.class))
          .forEach(f -> register().from(f));
      Util.getTopMethods(clz)
          .stream()
          .filter(m -> m.isAnnotationPresent(Bean.class))
          .forEach(f -> register().from(f));

      if (scan.currentPackage()) {
        scan(false, clz.getPackage().getName());
      }
      Arrays.stream(scan.packages()).forEach(p -> {
        Class<?> c = p.type();
        String name;
        if (c != void.class) {
          name = c.getPackage().getName();
        } else {
          name = p.name();
        }
        scan(p.inherit(), name);
      });
      Arrays.stream(scan.classes()).forEach(this::scan);
    }
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
      debug("Register bean from class: " + clz);
    }

    @Override
    public void from(Field field) throws IllegalDefineException {
      BeanFactory<T> factory = new FieldBeanFactory<>(field, scope, qualifier);
      autoImpl(factory, field);
      addToRepository(factory);
      debug("Register bean from field: " + field);
    }

    @Override
    public void from(Method method) throws IllegalDefineException {
      BeanFactory<T> factory = new MethodBeanFactory<>(method, scope, qualifier);
      autoImpl(factory, method);
      addToRepository(factory);
      debug("Register bean from method: " + method);
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
    public Optional<Provider<T>> getProvider() {
      return factories.getFactory(clz)
          .stream()
          .filter(f -> f.getQualifier().match(qualifier))
          .findFirst()
          .<Provider<T>> map(f -> factories.getProvider(f));
    }

    @Override
    public BeanQuery<T> qualifies(Qualifier qualifier) {
      this.qualifier = this.qualifier.and(qualifier);
      return this;
    }
  }
}
