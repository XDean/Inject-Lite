package xdean.inject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.function.IntSupplier;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;

import xdean.inject.annotation.Bean;
import xdean.inject.impl.BeanRepositoryImpl;

public class InjectTest {
  BeanRepository repo;

  @Before
  public void setup() {
    repo = new BeanRepositoryImpl();
    repo.register(ServiceImpl.class);
    repo.register(IdGenerator.class);
  }

  @Test
  public void test() throws Exception {
    User user = repo.query(User.class).get().get();
    assertNotNull(user);
    assertNotNull(user.service);
    assertTrue(user.service instanceof ServiceImpl);
    assertEquals(1, user.id);
    assertNotNull(user.manager);
  }

  public interface Service {
  }

  @Bean(Service.class)
  public static class ServiceImpl implements Service {
  }

  @Bean(IntSupplier.class)
  public static class IdGenerator implements IntSupplier {
    @Override
    public int getAsInt() {
      return 1;
    }
  }

  @Bean
  public static class Manager {
  }

  public static class User {
    @Inject
    private Service service;

    final int id;
    Manager manager;

    @Inject
    public User(IntSupplier is) {
      id = is.getAsInt();
    }

    @Inject
    private void init(Manager manager) {
      this.manager = manager;
    }
  }
}
