package tech.iooo.boot.example.service;

import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

/**
 * Created on 2018-12-10 14:54
 *
 * @author <a href="mailto:yangkizhang@gmail.com?subject=iooo-boot">Ivan97</a>
 */
@Service
public class CacheCommandLineRunner implements CommandLineRunner {

  private static final Logger logger = LoggerFactory.getLogger(CacheCommandLineRunner.class);
  @Autowired
  private IUserService userService;
  @Autowired
  private Vertx vertx;

  @Override
  public void run(String... args) throws Exception {
    vertx.setPeriodic(1000, handler -> {
      if (logger.isInfoEnabled()) {
        logger.info("{}", userService.getUser());
      }
    });
  }
}
