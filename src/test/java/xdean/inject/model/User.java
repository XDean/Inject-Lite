package xdean.inject.model;

import java.util.function.IntSupplier;

import javax.inject.Inject;

import lombok.Data;
import xdean.inject.annotation.Bean;

@Bean
@Data
public class User {
  @Inject
  private Service service;

  final int id;
  private Manager manager;

  @Inject
  public User(IntSupplier is) {
    id = is.getAsInt();
  }

  @Inject
  private void init(Manager manager) {
    this.manager = manager;
  }
}