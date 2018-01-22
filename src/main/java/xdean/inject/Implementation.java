package xdean.inject;

/**
 * For Scope
 *
 * @author XDean
 *
 * @param <T>
 */
public interface Implementation<T> {
  T get(InjectRepository repo);
}