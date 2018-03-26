package xdean.inject.impl.factory;

import java.util.Stack;

import io.reactivex.Observable;
import xdean.inject.impl.BeanFactory;

public class BeanFactoryContext {
  private static final ThreadLocal<Stack<BeanFactory<?>>> FACTORIES = new ThreadLocal<Stack<BeanFactory<?>>>() {
    @Override
    protected Stack<BeanFactory<?>> initialValue() {
      return new Stack<>();
    };
  };

  public static void push(BeanFactory<?> bf) {
    Stack<BeanFactory<?>> stack = FACTORIES.get();
    if (stack.contains(bf)) {
      throw new IllegalStateException("Cyclical dependency happens: " +
          Observable.fromIterable(stack)
              .skipWhile(e -> !e.equals(bf))
              .concatWith(Observable.just(bf))
              .map(b -> b.getIdentifier())
              .reduce((a, b) -> a + " -> " + b)
              .blockingGet());
    }
    stack.push(bf);
  }

  public static void pop(BeanFactory<?> bf) {
    BeanFactory<?> pop = FACTORIES.get().pop();
    if (pop != bf) {
      throw new IllegalStateException("Except pop: " + bf + ", but was: " + pop);
    }
  }
}
