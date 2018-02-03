package xdean.inject.old.model;

import java.util.Optional;

import xdean.inject.old.Implementation;
import xdean.inject.old.InjectRepositoryImpl;

public class QualifierWrapper<T> {

  private final Implementation<? extends T> actual;
  private final Qualifier qualifier;

  public QualifierWrapper(Class<? extends T> clz, Implementation<? extends T> actual) {
    this.actual = actual;
    this.qualifier = Qualifier.from(clz);
  }

  public boolean match(Qualifier target) {
    return qualifier.match(target);
  }

  public Optional<T> get(InjectRepositoryImpl repo, Qualifier target) {
    if (this.qualifier.match(target)) {
      return Optional.of(actual.get(repo));
    } else {
      return Optional.empty();
    }
  }
}
