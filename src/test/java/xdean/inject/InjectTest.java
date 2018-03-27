package xdean.inject;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.runner.RunWith;

import xdean.inject.impl.BeanRepositoryImpl;

@Ignore
@RunWith(InjectRunner.class)
public class InjectTest {

  protected BeanRepository repo;

  @Before
  public void setup() {
    repo = new BeanRepositoryImpl().scan(getClass());
  }
}
