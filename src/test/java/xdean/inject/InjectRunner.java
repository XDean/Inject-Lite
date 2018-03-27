package xdean.inject;

import static xdean.inject.exception.BeanNotFoundException.notFound;
import static xdean.inject.exception.IllegalDefineException.assertThat;
import static xdean.jex.util.lang.ExceptionUtil.uncheck;

import java.lang.annotation.Annotation;
import java.lang.reflect.Executable;
import java.lang.reflect.Field;
import java.lang.reflect.Parameter;
import java.util.List;

import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;

public class InjectRunner extends BlockJUnit4ClassRunner {

  private final Field getRepo;

  public InjectRunner(Class<?> klass) throws InitializationError {
    super(klass);
    assertThat(InjectTest.class.isAssignableFrom(klass), "Must extends InjectTest.");
    getRepo = uncheck(() -> InjectTest.class.getDeclaredField("repo"));
    getRepo.setAccessible(true);
  }

  @Override
  protected Statement methodInvoker(FrameworkMethod method, Object test) {
    return new Statement() {
      @Override
      public void evaluate() throws Throwable {
        BeanRepository repo = (BeanRepository) uncheck(() -> getRepo.get(test));
        Object[] args = prepareArgument(method.getMethod(), repo);
        method.invokeExplosively(test, args);
      }
    };
  }

  @Override
  protected void validatePublicVoidNoArgMethods(Class<? extends Annotation> annotation, boolean isStatic,
      List<Throwable> errors) {
    List<FrameworkMethod> methods = getTestClass().getAnnotatedMethods(annotation);
    for (FrameworkMethod eachTestMethod : methods) {
      eachTestMethod.validatePublicVoid(isStatic, errors);
    }
  }

  static Object[] prepareArgument(Executable element, BeanRepository repo) {
    Parameter[] parameters = element.getParameters();
    Object[] params = new Object[element.getParameterCount()];
    for (int i = 0; i < params.length; i++) {
      Parameter p = parameters[i];
      Qualifier q = Qualifier.from(p);
      Object o = repo.query(p.getType()).qualifies(q).get().orElseThrow(notFound(repo, p.getType(), q));
      params[i] = o;
    }
    return params;
  }
}
