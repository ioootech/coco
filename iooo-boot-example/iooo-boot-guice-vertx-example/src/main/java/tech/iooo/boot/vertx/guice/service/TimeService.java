package tech.iooo.boot.vertx.guice.service;

import com.google.inject.Singleton;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created on 2018-12-01 14:04
 *
 * @author <a href="mailto:yangkizhang@gmail.com?subject=iooo-boot">Ivan97</a>
 */
@Singleton
public class TimeService {

  private static final Logger logger = LoggerFactory.getLogger(TimeService.class);

  public String now() {
    String time = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    if (logger.isInfoEnabled()) {
      logger.info("{}", time);
    }
    return time;
  }
}
