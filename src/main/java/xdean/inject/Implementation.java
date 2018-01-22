package xdean.inject;

/**
 * For Scope
 *
 * @author XDean
 *
 * @param <T>
 */
interface Implementation<T> {
  T get(InjectRepository repo);
}