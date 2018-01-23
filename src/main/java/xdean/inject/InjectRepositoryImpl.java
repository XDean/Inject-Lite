package xdean.inject;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import javax.inject.Provider;

import xdean.inject.api.InjectRepository;
import xdean.inject.model.Qualifier;

@SuppressWarnings("unchecked")
public class InjectRepositoryImpl implements InjectRepository {

  public static final InjectRepositoryImpl GLOBAL = new InjectRepositoryImpl();

  private final Map<Class<?>, TypeRepository<?>> typeRepos = new HashMap<>();
  private final List<InjectRepositoryImpl> children = new LinkedList<>();

  private InjectRepositoryImpl() {
  }

  private InjectRepositoryImpl(InjectRepositoryImpl parent) {
    parent.children.add(this);
  }

  /**
   * Register a class into the repository
   */
  public <T> void register(Class<T> type) {
    register(type, type);
  }

  /**
   * Register an implementation for a type
   */
  public <T> void register(Class<T> type, Class<? extends T> impl) {
    findTypeRepo(type).register(impl);
  }

  /**
   * Inject fields for the given instance
   */
  public <T> void inject(T impl) {
    // TODO
  }

  /**
   * Get instance of the type
   */
  public <T> T get(Class<T> type) {
    return findTypeRepo(type).get(this).orElseThrow(instanceNotFound(type));
  }

  /**
   * Get instance of the type with specific qualifier
   */
  public <T> T get(Class<T> type, Qualifier qualifier) {
    return findTypeRepo(type).get(this, qualifier).orElseThrow(instanceNotFound(type));
  }

  public <T> Provider<T> getProvider(Class<T> type) {
    return findTypeRepo(type).getProvider(this).orElseThrow(instanceNotFound(type));
  }

  public <T> Provider<T> getProvider(Class<T> type, Qualifier qualifier) {
    return findTypeRepo(type).getProvider(this, qualifier).orElseThrow(instanceNotFound(type));
  }

  private <T> TypeRepository<T> findTypeRepo(Class<T> type) {
    return (TypeRepository<T>) typeRepos.computeIfAbsent(type, k -> new TypeRepository<>());
  }

  private <T> Supplier<IllegalStateException> instanceNotFound(Class<T> type) {
    return () -> new IllegalStateException("No instance of " + type);
  }
}
