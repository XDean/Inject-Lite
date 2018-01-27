package xdean.inject;

import static org.junit.Assert.*;

import java.util.function.IntSupplier;

import javax.inject.Inject;
import javax.inject.Provider;

import org.junit.Before;
import org.junit.Test;

import xdean.inject.impl.old.InjectRepositoryImpl;

public class TestProvider {
  @Before
  public void setup() {
    InjectRepositoryImpl.GLOBAL.register(Service.class, ServiceImpl.class);
    InjectRepositoryImpl.GLOBAL.register(User.class);
    InjectRepositoryImpl.GLOBAL.register(Manager.class);
    InjectRepositoryImpl.GLOBAL.register(IdGenerator.class);
  }

  @Test
  public void test() throws Exception {
    IdGenerator.i = 0;
    User user = InjectRepositoryImpl.GLOBAL.get(User.class);
    assertNotNull(user);
    assertNotNull(user.service);
    assertTrue(user.service.get() instanceof ServiceImpl);
    assertEquals(1, user.id);
    assertNotNull(user.manager1);
    assertNotNull(user.manager2);
    assertNotSame(user.manager1, user.manager2);
  }

  public interface Service {
  }

  public static class ServiceImpl implements Service {
  }

  public static class IdGenerator implements IntSupplier {
    static int i = 0;
    int index;

    public IdGenerator() {
      index = i++;
    }

    @Override
    public int getAsInt() {
      return index;
    }
  }

  public static class Manager {
  }

  public static class User {
    @Inject
    Provider<Service> service;
    final int id;
    Manager manager1, manager2;

    @Inject
    public User(Provider<IdGenerator> idGenerator) {
      idGenerator.get();
      id = idGenerator.get().getAsInt();
    }

    @Inject
    private void init(Provider<Manager> manager) {
      this.manager1 = manager.get();
      this.manager2 = manager.get();
    }
  }
}
