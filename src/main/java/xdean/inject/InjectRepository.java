package xdean.inject;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.inject.Provider;

@SuppressWarnings("unchecked")
public class InjectRepository {

  public static final InjectRepository GLOBAL = new InjectRepository();

  private InjectRepository parent;
  private final Map<Class<?>, TypeRepository<?>> typeRepos = new HashMap<>();
  private final List<InjectRepository> children = new LinkedList<>();

  private InjectRepository() {

  }

  public <T> void register(Class<T> type, Class<? extends T> impl) {
    findTypeRepo(type).register(impl);
  }

  public <T> T get(Class<T> type) {
    return findTypeRepo(type).get();
  }

  <T> Provider<T> getProvider(Class<T> type) {
    return findTypeRepo(type).getProvider();
  }

  private <T> TypeRepository<T> findTypeRepo(Class<T> type) {
    return (TypeRepository<T>) typeRepos.computeIfAbsent(type, k -> new TypeRepository<T>(this));
  }
}
