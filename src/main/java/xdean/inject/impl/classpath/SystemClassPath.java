package xdean.inject.impl.classpath;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

import io.reactivex.Flowable;
import xdean.inject.ClassPath;
import xdean.jex.log.Logable;
import xdean.jex.util.file.FileUtil;

public class SystemClassPath implements ClassPath, Logable {
  @Override
  public Flowable<Class<?>> scan(String pckg, boolean inherit) {
    URL resource = Object.class.getResource("/" + pckg.replace('.', '/') + "/");
    if (resource == null) {
      info(String.format("No package named %s in system class path.", pckg));
      return Flowable.empty();
    }
    try {
      Path root = Paths.get(resource.toURI());
      return FileUtil.wideTraversal(root)
          .filter(p -> p.getFileName().toString().endsWith(".class"))
          .map(p -> Class.forName(pckg + "." + root.relativize(p).toString()
              .replace('/', '.')
              .replace('\\', '.')
              .replaceAll(".class$", "")));
    } catch (URISyntaxException e) {
      return Flowable.error(e);
    }
  }
}
