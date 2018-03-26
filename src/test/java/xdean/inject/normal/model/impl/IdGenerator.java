package xdean.inject.normal.model.impl;

import java.util.function.IntSupplier;

import xdean.inject.annotation.Bean;

@Bean(IntSupplier.class)
public class IdGenerator implements IntSupplier {
  @Override
  public int getAsInt() {
    return 1;
  }
}