package xdean.inject;

import static org.junit.Assert.*;

import javax.inject.Inject;

import org.junit.Test;

public class TestInject {
  @Test
  public void test() throws Exception {
    InjectRepository.GLOBAL.register(Service.class, ServiceImpl.class);
    User user = InjectRepository.GLOBAL.get(User.class);
    assertNotNull(user);
    assertNotNull(user.service);
    assertTrue(user.service instanceof ServiceImpl);
  }

  public interface Service {

  }

  public static class ServiceImpl implements Service{

  }

  public class User {
    @Inject
    Service service;
  }
}
