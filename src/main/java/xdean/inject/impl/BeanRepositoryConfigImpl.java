package xdean.inject.impl;

import java.util.ArrayList;
import java.util.List;

import xdean.inject.BeanRepositoryConfig;
import xdean.inject.ClassPath;
import xdean.inject.impl.classpath.SystemClassPath;

public class BeanRepositoryConfigImpl implements BeanRepositoryConfig {

  boolean autoRegister = true;
  List<ClassPath> classpaths = new ArrayList<>();

  public BeanRepositoryConfigImpl() {
    classpaths.add(new SystemClassPath());
  }

  @Override
  public BeanRepositoryConfig autoRegister(boolean auto) {
    autoRegister = auto;
    return this;
  }

  @Override
  public BeanRepositoryConfig addClasspath(ClassPath cp) {

    return this;
  }

  void loadConfig(Class<?> clz) {

  }
}
