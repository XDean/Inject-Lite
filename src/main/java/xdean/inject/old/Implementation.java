package xdean.inject.old;

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