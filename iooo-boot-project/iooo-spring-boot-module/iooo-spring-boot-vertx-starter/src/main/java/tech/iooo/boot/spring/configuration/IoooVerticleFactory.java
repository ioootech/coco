package tech.iooo.boot.spring.configuration;

import io.vertx.core.Promise;
import io.vertx.core.Verticle;
import io.vertx.core.Vertx;
import io.vertx.core.spi.VerticleFactory;
import java.util.concurrent.Callable;
import org.springframework.stereotype.Component;

/**
 * Created on 2018/9/3 上午10:08
 *
 * @author <a href="mailto:yangkizhang@gmail.com?subject=iooo-boot">Ivan97</a>
 */
@Component
public class IoooVerticleFactory implements VerticleFactory {

  @Override
  public String prefix() {
    return VertxConfigConstants.IOOO_VERTICLE_PREFIX;
  }

  /**
   * Create a verticle instance. If this method is likely to be slow (e.g. Ruby or JS verticles which might have to start up a language engine) then make sure it is run on a worker thread by {@link Vertx#executeBlocking}.
   *
   * @param verticleName The verticle name
   * @param classLoader The class loader
   * @param promise the promise to complete with the result
   */
  @Override
  public void createVerticle(String verticleName, ClassLoader classLoader, Promise<Callable<Verticle>> promise) {
    String clazz = VerticleFactory.removePrefix(verticleName);
    promise.complete(() -> IoooVerticleServicesHolder.activeVerticleServices().rowMap().get(clazz).values().iterator().next());
  }
}
