package xdean.inject;

import static org.junit.Assert.assertSame;

import javax.inject.Singleton;

import org.junit.Before;
import org.junit.Test;

import xdean.inject.impl.old.InjectRepositoryImpl;

public class TestScope {
  @Before
  public void setup() {
    InjectRepositoryImpl.GLOBAL.register(Service.class, ServiceImpl.class);
  }

  @Test
  public void testName() throws Exception {
    Service a = InjectRepositoryImpl.GLOBAL.get(Service.class);
    Service b = InjectRepositoryImpl.GLOBAL.get(Service.class);
    assertSame(a, b);
  }

  public interface Service {
  }

  @Singleton
  public static class ServiceImpl implements Service {
  }
}
