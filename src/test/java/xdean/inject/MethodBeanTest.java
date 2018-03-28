package xdean.inject;

import javax.inject.Named;
import javax.inject.Singleton;

import static org.junit.Assert.*;
import org.junit.Test;

import lombok.AllArgsConstructor;
import xdean.inject.annotation.Bean;
import xdean.inject.annotation.Scan;

@Scan
public class MethodBeanTest extends InjectTest {
  @Test
  public void test(@Named("1") A a11, @Named("1") A a12, @Named("2") A a21, @Named("2") A a22) throws Exception {
    assertEquals(1, a11.id);
    assertEquals(1, a12.id);
    assertEquals(2, a21.id);
    assertEquals(2, a22.id);
    assertSame(a11, a12);
    assertNotSame(a21, a22);
  }

  @Bean
  @Singleton
  @Named("1")
  private static A a1() {
    return new A(1);
  }

  @Bean
  @Named("2")
  private A a2() {
    return new A(2);
  }

  @AllArgsConstructor
  public static class A {
    int id;
  }
}
