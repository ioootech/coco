package tech.iooo.boot.guice.vertx;

import com.google.inject.Injector;
import io.vertx.core.Promise;
import io.vertx.core.Verticle;
import io.vertx.core.Vertx;
import io.vertx.core.spi.VerticleFactory;
import java.lang.reflect.Constructor;
import java.util.concurrent.Callable;
import lombok.SneakyThrows;

/**
 * Created on 2018-12-01 13:41
 *
 * @author <a href="mailto:yangkizhang@gmail.com?subject=iooo-boot">Ivan97</a>
 */
public class GuiceVerticleFactory implements VerticleFactory {

  private Injector injector;

  /**
   * Returns the current parent injector
   */
  public Injector getInjector() {
    if (injector == null) {
      injector = createInjector();
    }
    return injector;
  }

  /**
   * Sets the parent injector
   */
  public GuiceVerticleFactory setInjector(Injector injector) {
    this.injector = injector;
    return this;
  }


  @Override
  public String prefix() {
    return GuiceVertxConstants.VERTICLE_PREFIX;
  }

  /**
   * Create a verticle instance. If this method is likely to be slow (e.g. Ruby or JS verticles which might have to start up a language engine) then make sure it is run on a worker thread by {@link Vertx#executeBlocking}.
   *
   * @param verticleName The verticle name
   * @param classLoader The class loader
   * @param promise the promise to complete with the result
   */
  @Override
  @SneakyThrows
  public void createVerticle(String verticleName, ClassLoader classLoader, Promise<Callable<Verticle>> promise) {
    verticleName = VerticleFactory.removePrefix(verticleName);

    // Use the provided class loader to create an instance of GuiceVerticleLoader.  This is necessary when working with vert.x IsolatingClassLoader
    @SuppressWarnings("unchecked")
    Class<Verticle> loader = (Class<Verticle>) classLoader.loadClass(GuiceVerticleLoader.class.getName());
    Constructor<Verticle> ctor = loader.getConstructor(String.class, ClassLoader.class, Injector.class);

    String finalVerticleName = verticleName;
    promise.complete(() -> ctor.newInstance(finalVerticleName, classLoader, getInjector()));
  }


  protected Injector createInjector() {
    return null;
  }
}
