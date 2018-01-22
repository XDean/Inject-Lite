package xdean.inject;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import javax.inject.Provider;

import xdean.inject.model.Qualifier;

@SuppressWarnings("unchecked")
public class InjectRepository {

  public static final InjectRepository GLOBAL = new InjectRepository();

  private final Map<Class<?>, TypeRepository<?>> typeRepos = new HashMap<>();
  private final List<InjectRepository> children = new LinkedList<>();

  private InjectRepository() {
  }

  private InjectRepository(InjectRepository parent) {
    parent.children.add(this);
  }

  public <T> void register(Class<T> type) {
    findTypeRepo(type).register(type);
  }

  public <T> void register(Class<T> type, Class<? extends T> impl) {
    findTypeRepo(type).register(impl);
  }

  public <T> T get(Class<T> type) {
    return findTypeRepo(type).get(this).orElseThrow(instanceNotFound(type));
  }

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
