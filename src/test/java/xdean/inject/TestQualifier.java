package xdean.inject;

import static org.junit.Assert.*;

import javax.inject.Inject;
import javax.inject.Named;

import org.junit.Before;
import org.junit.Test;

public class TestQualifier {
  @Before
  public void setup() {
    InjectRepositoryImpl.GLOBAL.register(Service.class, ServiceImpl1.class);
    InjectRepositoryImpl.GLOBAL.register(Service.class, ServiceImpl2.class);
    InjectRepositoryImpl.GLOBAL.register(User.class);
  }

  @Test
  public void testNamed() throws Exception {
    User u = InjectRepositoryImpl.GLOBAL.get(User.class);
    assertNotNull(u.a);
    assertTrue(u.a instanceof ServiceImpl1);
    assertNotNull(u.b);
    assertTrue(u.b instanceof ServiceImpl2);
  }

  public interface Service {
  }

  @Named("1")
  public static class ServiceImpl1 implements Service {
  }

  @Named("2")
  public static class ServiceImpl2 implements Service {
  }

  public static class User {
    private @Inject @Named("1") Service a;

    public @Inject @Named("2") Service b;
  }
}
