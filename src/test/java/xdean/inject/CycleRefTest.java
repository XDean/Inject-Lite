package xdean.inject;

import javax.inject.Inject;

import org.junit.Test;

import lombok.Data;
import xdean.inject.annotation.Bean;
import xdean.inject.annotation.Scan;

@Scan
public class CycleRefTest extends InjectTest {

  @Test
  public void test() throws Exception {
    System.out.println(repo.getBean(A.class));
  }

  @Bean
  @Data
  public static class A {
    @Inject
    B b;
  }

  @Bean
  @Data
  public static class B {
    @Inject
    A a;
  }
}
