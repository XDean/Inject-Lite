package xdean.inject;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.junit.Test;

import com.google.common.base.MoreObjects;

import xdean.inject.annotation.Bean;
import xdean.inject.annotation.Scan;

@Scan
public class CycleRefTest extends InjectTest {

  @Test
  public void test() throws Exception {
    System.out.println(repo.getBean(A.class).get());
    System.out.println(repo.getBean(B.class).get());
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

  @Singleton
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
}
