package xdean.inject;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import javax.inject.Provider;

import xdean.inject.model.Qualifier;
import xdean.inject.model.QualifierWrapper;

class TypeRepository<T> {
  private List<QualifierWrapper<? extends T>> impls = new LinkedList<>();

  TypeRepository() {
  }

  <K extends T> void register(Class<K> impl) {
    impls.add(new QualifierWrapper<>(impl, new DefaultImpl<>(impl)));
  }

  private boolean hasImpl(Qualifier target) {
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

  public Optional<? extends T> get(InjectRepository repo, Qualifier target) {
    return impls.stream()
        .map(qw -> qw.get(repo, target))
        .filter(Optional::isPresent)
        .map(Optional::get)
        .findFirst();
  }
}