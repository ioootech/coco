package tech.iooo.boot.guice.vertx;

import com.google.inject.Injector;
import io.vertx.core.Verticle;
import io.vertx.core.spi.VerticleFactory;
import java.lang.reflect.Constructor;

/**
 * Created on 2018-12-01 13:41
 *
 * @author <a href="mailto:yangkizhang@gmail.com?subject=iooo-boot">Ivan97</a>
 */
public class GuiceVerticleFactory implements VerticleFactory {

  public static final String PREFIX = "java-guice";
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
    return PREFIX;
  }

  @Override
  public Verticle createVerticle(String verticleName, ClassLoader classLoader) throws Exception {
    verticleName = VerticleFactory.removePrefix(verticleName);

    // Use the provided class loader to create an instance of GuiceVerticleLoader.  This is necessary when working with vert.x IsolatingClassLoader
    @SuppressWarnings("unchecked")
    Class<Verticle> loader = (Class<Verticle>) classLoader.loadClass(GuiceVerticleLoader.class.getName());
    Constructor<Verticle> ctor = loader.getConstructor(String.class, ClassLoader.class, Injector.class);

    if (ctor == null) {
      throw new IllegalStateException("Could not find GuiceVerticleLoader constructor");
    }

    return ctor.newInstance(verticleName, classLoader, getInjector());
  }

  protected Injector createInjector() {
    return null;
  }
}
