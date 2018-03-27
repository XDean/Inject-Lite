package xdean.inject.normal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import xdean.inject.InjectTest;
import xdean.inject.annotation.Scan;
import xdean.inject.normal.model.Service;
import xdean.inject.normal.model.User;
import xdean.inject.normal.model.impl.ServiceImpl;

@Scan(typeSafePackages = { Service.class, ServiceImpl.class })
public class NormalTest extends InjectTest {
  @Test
  public void test(User user) throws Exception {
    assertNotNull(user);
    assertNotNull(user.getService());
    assertTrue(user.getService() instanceof ServiceImpl);
    assertEquals(1, user.getId());
    assertNotNull(user.getManager());
  }
}
