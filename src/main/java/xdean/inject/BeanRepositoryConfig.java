package xdean.inject;

public interface BeanRepositoryConfig {
  BeanRepositoryConfig autoRegister(boolean auto);

  BeanRepositoryConfig addClasspath(ClassPath cp);
}
