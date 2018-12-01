package tech.iooo.boot.vertx.guice.configuration;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import io.vertx.core.Vertx;

/**
 * Created on 2018-12-01 14:03
 *
 * @author <a href="mailto:yangkizhang@gmail.com?subject=iooo-boot">Ivan97</a>
 */
public class IoooBootModule extends AbstractModule {

  @Override
  protected void configure() {

  }

  @Provides
  public Vertx vertx() {
    return Vertx.vertx();
  }
}
