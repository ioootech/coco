package tech.iooo.boot.guice.vertx;

import static tech.iooo.boot.guice.vertx.GuiceVertxConstants.BOOTSTRAP_BINDER_NAME;
import static tech.iooo.boot.guice.vertx.GuiceVertxConstants.CONFIG_BOOTSTRAP_BINDER_NAME;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Module;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Context;
import io.vertx.core.Promise;
import io.vertx.core.Verticle;
import io.vertx.core.Vertx;
import io.vertx.core.impl.verticle.CompilingClassLoader;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import java.util.ArrayList;
import java.util.List;
import net.sf.cglib.proxy.Enhancer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created on 2018-12-01 13:45
 *
 * @author <a href="mailto:yangkizhang@gmail.com?subject=iooo-boot">Ivan97</a>
 *
 * Guice Verticle to lazy load the real verticle with DI
 */
public class GuiceVerticleLoader extends AbstractVerticle {

  private static final Logger logger = LoggerFactory.getLogger(GuiceVerticleLoader.class);
  private final ClassLoader classLoader;
  private final String verticleName;
  private final Injector parent;
  private Verticle realVerticle;

  public GuiceVerticleLoader(String verticleName, ClassLoader classLoader, Injector parent) {
    this.verticleName = verticleName;
    this.classLoader = classLoader;
    this.parent = parent;
  }

  /**
   * Initialise the verticle.<p> This is called by Vert.x when the verticle instance is deployed. Don't call it yourself.
   *
   * @param vertx the deploying Vert.x instance
   * @param context the context of the verticle
   */
  @Override
  public void init(Vertx vertx, Context context) {
    super.init(vertx, context);

    try {
      // Create the real verticle and init
      realVerticle = createRealVerticle();
      realVerticle.init(vertx, context);
    } catch (Throwable t) {
      if (t instanceof RuntimeException) {
        throw (RuntimeException) t;
      }
      throw new RuntimeException(t);
    }
  }

  /**
   * Override this method to signify that start is complete sometime _after_ the start() method has returned This is useful if your verticle deploys other verticles or modules and
   * you don't want this verticle to be considered started until the other modules and verticles have been started.
   *
   * @param startedResult When you are happy your verticle is started set the result
   */
  @Override
  public void start(Promise<Void> startedResult) throws Exception {
    // Start the real verticle
    realVerticle.start(startedResult);
  }

  /**
   * Vert.x calls the stop method when the verticle is undeployed. Put any cleanup code for your verticle in here
   */
  @Override
  public void stop(Promise<Void> stopFuture) throws Exception {
    // Stop the real verticle
    if (realVerticle != null) {
      realVerticle.stop(stopFuture);
      realVerticle = null;
    }
  }

  public String getVerticleName() {
    return verticleName;
  }

  public Verticle createRealVerticle() throws Exception {
    String className = verticleName;
    Class<?> clazz;

    if (className.endsWith(".java")) {
      CompilingClassLoader compilingLoader = new CompilingClassLoader(classLoader, className);
      className = compilingLoader.resolveMainClassName();
      clazz = compilingLoader.loadClass(className);
    } else {
      clazz = classLoader.loadClass(className);
    }
    Verticle verticle = createRealVerticle(clazz);
    Enhancer enhancer = new Enhancer();
    enhancer.setSuperclass(clazz);
    enhancer.setCallback(new VerticleWrapper<>(verticle));
    return (Verticle) enhancer.create();
  }

  private Verticle createRealVerticle(Class<?> clazz) throws Exception {

    JsonObject config = config();
    Object field = config.getValue(CONFIG_BOOTSTRAP_BINDER_NAME);
    JsonArray bootstrapNames;
    List<Module> bootstraps = new ArrayList<>();

    if (field instanceof JsonArray) {
      bootstrapNames = (JsonArray) field;
    } else {
      bootstrapNames = new JsonArray().add((field == null ? BOOTSTRAP_BINDER_NAME : field));
    }

    for (int i = 0; i < bootstrapNames.size(); i++) {
      String bootstrapName = bootstrapNames.getString(i);
      try {
        Class<?> bootstrapClass = classLoader.loadClass(bootstrapName);
        Object obj = bootstrapClass.newInstance();

        if (obj instanceof Module) {
          bootstraps.add((Module) obj);
        } else {
          logger.error("Class " + bootstrapName + " does not implement Module.");
        }
      } catch (ClassNotFoundException e) {
        if (parent == null) {
          logger.warn("Guice bootstrap binder class " + bootstrapName
              + " was not found.  Are you missing injection bindings?");
        }
      }
    }

    // Add vert.x binder if not already bound
    if (parent == null || parent.getExistingBinding(Key.get(Vertx.class)) == null) {
      bootstraps.add(new GuiceVertxModuleAdapter(vertx));
    }

    // Each verticle FACTORY will have it's own (child) injector instance
    Injector injector = parent == null ? Guice.createInjector(bootstraps) : parent.createChildInjector(bootstraps);
    Enhancer enhancer = new Enhancer();
    enhancer.setSuperclass(clazz);
    enhancer.setCallback(new VerticleWrapper<>((Verticle) injector.getInstance(clazz)));
    return (Verticle) enhancer.create();
  }
}
