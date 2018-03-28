package xdean.inject.impl;

import org.junit.Test;

import com.google.common.collect.ImmutableSet;

import io.reactivex.Observable;
import xdean.inject.impl.Util;

public class UtilTest {
  @Test
  public void testGetTopMethod() throws Exception {
    Observable.fromIterable(Util.getTopMethods(B.class))
        .filter(m -> m.getDeclaringClass() != Object.class)
        .test()
        .assertValueCount(3)
        .assertValueSet(ImmutableSet.of(
            B.class.getDeclaredMethod("func"),
            A.class.getDeclaredMethod("func", int.class),
            A.class.getDeclaredMethod("bar")));
  }

  static class A {
    void func() {
    }

    void func(int i) {
    }

    void bar() {
    };
  }

  static class B extends A {
    @Override
    void func() {
      super.func();
    }
  }
}