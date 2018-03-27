package xdean.inject;

import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.junit.Test;

import com.google.common.base.MoreObjects;

import io.reactivex.Observable;
import lombok.AllArgsConstructor;
import xdean.inject.annotation.Bean;
import xdean.inject.annotation.Scan;

@Scan
public class CycleRefTest extends InjectTest {

  @Test
  public void testResolve(A a, B b) throws Exception {
    assertSame(a, a.b.a);
    assertSame(a, b.a);
    assertSame(a.b, b.a.b);
    assertNotSame(a.b, b);
  }

  @Test
  public void testError() throws Exception {
    Observable.fromCallable(() -> repo.getBean(C.class))
        .test()
        .assertError(IllegalStateException.class)
        .assertError(e -> e.getMessage().contains("Cyclical dependency happens"));
  }

  @Singleton
  @Bean
  public static class A {
    @Inject
    B b;

    @Override
    public String toString() {
      return MoreObjects.toStringHelper(this)
          .add("id", System.identityHashCode(this))
          .add("b", System.identityHashCode(b))
          .toString();
    }
  }

  @Bean
  public static class B {
    @Inject
    A a;

    @Override
    public String toString() {
      return MoreObjects.toStringHelper(this)
          .add("id", System.identityHashCode(this))
          .add("a", System.identityHashCode(a))
          .toString();
    }
  }

  @Bean
  @AllArgsConstructor(onConstructor_ = @Inject)
  public static class C {
    D d;
  }

  @Bean
  public static class D {
    @Inject
    C c;
  }
}
