package xdean.inject;

import io.reactivex.Flowable;

public interface ClassPath {
  Flowable<Class<?>> scan(String pckg, boolean inherit);
}
