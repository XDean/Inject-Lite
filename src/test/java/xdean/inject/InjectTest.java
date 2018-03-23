package xdean.inject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import xdean.inject.annotation.Scan;
import xdean.inject.impl.BeanRepositoryImpl;
import xdean.inject.model.User;
import xdean.inject.model.impl.ServiceImpl;

@Scan(packages = { "xdean.inject.model", "xdean.inject.model.impl" })
public class InjectTest {
  BeanRepository repo;

  @Before
  public void setup() {
    repo = new BeanRepositoryImpl().scan(InjectTest.class);
  }

  @Test
  public void test() throws Exception {
    User user = repo.query(User.class).get().get();
    assertNotNull(user);
    assertNotNull(user.getService());
    assertTrue(user.getService() instanceof ServiceImpl);
    assertEquals(1, user.getId());
    assertNotNull(user.getManager());
  }
}
