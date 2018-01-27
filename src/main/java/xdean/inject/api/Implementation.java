package xdean.inject.api;

import xdean.inject.impl.old.InjectRepositoryImpl;

/**
 * For Scope
 *
 * @author XDean
 *
 * @param <T>
 */
public interface Implementation<T> {
  T get(InjectRepositoryImpl repo);
}