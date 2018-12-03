package tech.iooo.boot.guice.vertx;

import com.google.inject.AbstractModule;
import io.vertx.core.Vertx;

/**
 * Created on 2018-12-01 13:48
 *
 * @author <a href="mailto:yangkizhang@gmail.com?subject=iooo-boot">Ivan97</a>
 *
 * Guice {@link AbstractModule} for vertx and container injections
 */
public class GuiceVertxModuleAdapter extends AbstractModule {

  private final Vertx vertx;

  public GuiceVertxModuleAdapter(Vertx vertx) {
    this.vertx = vertx;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void configure() {
    bind(Vertx.class).toInstance(vertx);
  }
}
