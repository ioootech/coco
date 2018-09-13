package tech.iooo.boot;

import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.iooo.boot.core.constants.SuppressTypeConstants;

/**
 * Hello world!
 *
 * @author Ivan97
 */
public class App {

  private static final Logger logger = LoggerFactory.getLogger(App.class);

  @SuppressWarnings(SuppressTypeConstants.ALL)
  public static void main(String[] args) {
    logger.info("hello world");
    logger.debug("DEBUG");
    logger.trace("TRACE");
    Vertx.vertx().setPeriodic(1000, handler -> {
      logger.info("{}", System.currentTimeMillis());
    });
    logger.error("", new RuntimeException("Testing Error"));
  }
}
