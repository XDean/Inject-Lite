package xdean.inject;

import static org.junit.Assert.*;

import java.util.function.IntSupplier;

import javax.inject.Inject;

import org.junit.Test;

public class TestInject {
  @Test
  public void test() throws Exception {
    InjectRepository.GLOBAL.register(Service.class, ServiceImpl.class);
    InjectRepository.GLOBAL.registerSelf(User.class);
    InjectRepository.GLOBAL.registerSelf(Manager.class);
    InjectRepository.GLOBAL.registerSelf(IdGenerator.class);
    User user = InjectRepository.GLOBAL.get(User.class);
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
    Service service;

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
