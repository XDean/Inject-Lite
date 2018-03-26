package xdean.inject;

import org.junit.Before;

import xdean.inject.impl.BeanRepositoryImpl;

public class InjectTest {

  protected BeanRepository repo;

  @Before
  public void setup() {
    repo = new BeanRepositoryImpl().scan(getClass());
  }
}
