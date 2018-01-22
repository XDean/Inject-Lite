package xdean.inject;

import static org.junit.Assert.assertSame;

import javax.inject.Singleton;

import org.junit.Before;
import org.junit.Test;

public class TestScope {
  @Before
  public void setup() {
    InjectRepository.GLOBAL.register(Service.class, ServiceImpl.class);
  }

  @Test
  public void testName() throws Exception {
    Service a = InjectRepository.GLOBAL.get(Service.class);
    Service b = InjectRepository.GLOBAL.get(Service.class);
    assertSame(a, b);
  }

  public interface Service {
  }

  @Singleton
  public static class ServiceImpl implements Service {
  }
}
