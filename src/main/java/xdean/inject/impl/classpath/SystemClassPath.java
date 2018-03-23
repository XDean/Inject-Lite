package xdean.inject.impl.classpath;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

import io.reactivex.Flowable;
import xdean.inject.ClassPath;
import xdean.jex.log.Logable;

public class SystemClassPath implements ClassPath, Logable {
  @Override
  public Flowable<Class<?>> scan(String pckg) {
    URL resource = Object.class.getResource("/" + pckg.replace('.', '/') + "/");
    if (resource == null) {
      info(String.format("No package named %s in system class path.", pckg));
      return Flowable.empty();
    }
    try {
      return Flowable.fromIterable(Files.newDirectoryStream(Paths.get(resource.toURI())))
          .filter(p -> p.getFileName().toString().endsWith(".class"))
          .map(p -> Class.forName(pckg + "." + p.getFileName().toString().replaceAll(".class$", "")));
    } catch (URISyntaxException | IOException e) {
      return Flowable.error(e);
    }
  }
}
