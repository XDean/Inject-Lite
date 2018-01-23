package xdean.inject;

import static org.junit.Assert.*;

import java.util.function.IntSupplier;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;

public class TestInject {
  @Before
  public void setup() {
    InjectRepositoryImpl.GLOBAL.register(Service.class, ServiceImpl.class);
    InjectRepositoryImpl.GLOBAL.register(User.class);
    InjectRepositoryImpl.GLOBAL.register(Manager.class);
    InjectRepositoryImpl.GLOBAL.register(IdGenerator.class);
  }

  @Test
  public void test() throws Exception {
    User user = InjectRepositoryImpl.GLOBAL.get(User.class);
    assertNotNull(user);
    assertNotNull(user.service);
    assertTrue(user.service instanceof ServiceImpl);
    assertEquals(1, user.id);
    assertNotNull(user.manager);
  }

  public interface Service {
  }

  public static class ServiceImpl implements Service {
  }

  public static class IdGenerator implements IntSupplier {
    @Override
    public int getAsInt() {
      return 1;
    }
  }

  public static class Manager {
  }

  public static class User {
    @Inject
    private Service service;

    final int id;
    Manager manager;

    @Inject
    public User(IdGenerator idGenerator) {
      id = idGenerator.getAsInt();
    }

    @Inject
    private void init(Manager manager) {
      this.manager = manager;
    }
  }
}
