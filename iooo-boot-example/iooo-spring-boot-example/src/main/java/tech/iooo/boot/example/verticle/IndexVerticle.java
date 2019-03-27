package tech.iooo.boot.example.verticle;

import io.vertx.core.AbstractVerticle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.iooo.boot.cache.CacheKit;
import tech.iooo.boot.spring.annotation.VerticleService;

/**
 * Created on 2018/9/12 下午10:11
 *
 * @author <a href="mailto:yangkizhang@gmail.com?subject=iooo-boot">Ivan97</a>
 */
@VerticleService
public class IndexVerticle extends AbstractVerticle {

  private static final Logger logger = LoggerFactory.getLogger(IndexVerticle.class);

  @Override
  public void start() throws Exception {
    logger.info("welcome");
    vertx.setPeriodic(1000, id -> {
      CacheKit.set("mm", System.currentTimeMillis());
      if (logger.isInfoEnabled()) {
        logger.info("{}", CacheKit.get("mm"));
      }
    });
  }
}
