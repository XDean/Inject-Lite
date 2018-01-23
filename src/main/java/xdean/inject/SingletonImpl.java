package xdean.inject;

import java.util.concurrent.atomic.AtomicReference;

public class SingletonImpl<T> implements Implementation<T> {
  private final DefaultImpl<T> defaultImpl;
  private final AtomicReference<T> instance = new AtomicReference<>();

  public SingletonImpl(Class<? extends T> type) {
    this.defaultImpl = new DefaultImpl<>(type);
  }

  @Override
  public T get(InjectRepositoryImpl repo) {
    if (instance.get() == null) {
      instance.compareAndSet(null, defaultImpl.get(repo));
    }
    return instance.get();
  }
}
