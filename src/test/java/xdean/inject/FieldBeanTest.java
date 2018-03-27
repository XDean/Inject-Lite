package xdean.inject;

import static org.junit.Assert.*;
import javax.inject.Named;

import org.junit.Test;

import lombok.AllArgsConstructor;
import xdean.inject.annotation.Bean;
import xdean.inject.annotation.Scan;

@Scan
public class FieldBeanTest extends InjectTest {
  @Bean
  @Named("1")
  private static final A A1 = new A(1);

  @Bean
  @Named("2")
  private static final A A2 = new A(2);

  @Test
  public void test(@Named("1") A a1, @Named("2") A a2) throws Exception {
    assertEquals(1, a1.id);
    assertEquals(2, a2.id);
  }

  @AllArgsConstructor
  public static class A {
    int id;
  }
}
