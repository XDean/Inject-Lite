package xdean.inject.impl.factory;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Objects;
import java.util.Optional;

import io.reactivex.Observable;
import xdean.inject.impl.BeanFactory;
import xdean.jex.extra.collection.Pair;

@SuppressWarnings("rawtypes")
public class BeanFactoryContext {
  private static final ThreadLocal<Deque<Pair<BeanFactory, Object>>> FACTORIES = new ThreadLocal<Deque<Pair<BeanFactory, Object>>>() {
    @Override
    protected Deque<Pair<BeanFactory, Object>> initialValue() {
      return new ArrayDeque<>();
    };
  };

  @SuppressWarnings("unchecked")
  public static <T> T push(BeanFactory<T> bf, T o) {
    Deque<Pair<BeanFactory, Object>> stack = FACTORIES.get();
    Optional<Pair<BeanFactory, Object>> find = stack.stream().filter(p -> Objects.equals(p.getLeft(), bf)).findFirst();
    if (find.isPresent() && find.get().getRight() == null) {
      throw new IllegalStateException("Cyclical dependency happens: " +
          Observable.fromIterable(stack)
              .map(p -> p.getLeft())
              .takeWhile(e -> !e.equals(bf))
              .startWith(bf)
              .concatWith(Observable.just(bf))
              .map(b -> b.getIdentifier())
              .reduce((a, b) -> a + " <- " + b)
              .blockingGet());
    }
    stack.push(Pair.of(bf, o));
    return (T) find.map(p -> p.getRight()).orElse(null);
  }

  public static void pop(BeanFactory<?> bf) {
    Pair<BeanFactory, Object> pop = FACTORIES.get().pop();
    if (pop.getLeft() != bf) {
      throw new IllegalStateException("Except pop: " + bf + ", but was: " + pop.getLeft());
    }
  }
}
