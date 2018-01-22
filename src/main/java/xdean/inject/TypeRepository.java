package xdean.inject;

import java.util.LinkedList;
import java.util.List;

import javax.inject.Provider;

class TypeRepository<T> {
  private final InjectRepository injectRepo;

  TypeRepository(InjectRepository injectRepository) {
    injectRepo = injectRepository;
  }

  private List<Implementation> impls = new LinkedList<>();

  void register(Class<? extends T> impl) {
    impls.add(new ImplementationImpl<>(this, impl));
  }

  T get() {
    return getProvider().get();
  }

  Provider<T> getProvider() {
    return null;
  }
}