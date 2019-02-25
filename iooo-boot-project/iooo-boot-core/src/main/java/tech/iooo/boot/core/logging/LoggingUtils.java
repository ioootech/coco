package tech.iooo.boot.core.logging;

import lombok.experimental.UtilityClass;
import org.slf4j.Logger;
import org.slf4j.event.Level;

/**
 * Created on 2018-12-24 15:21
 *
 * @author <a href="mailto:yangkizhang@gmail.com?subject=iooo-boot">Ivan97</a>
 */
@UtilityClass
public class LoggingUtils {

  public void log(Logger logger, Level level, String msg) {
    switch (level) {
      case INFO:
        if (logger.isInfoEnabled()) {
          logger.info(msg);
        }
        break;
      case WARN:
        logger.warn(msg);
        break;
      case DEBUG:
        if (logger.isDebugEnabled()) {
          logger.debug(msg);
        }
        break;
      case ERROR:
        logger.error(msg);
        break;
      case TRACE:
        if (logger.isTraceEnabled()) {
          logger.trace(msg);
        }
        break;
      default:
        break;
    }
  }
  
  public void log(tech.iooo.boot.core.logging.Logger logger, tech.iooo.boot.core.logging.Level level){
  }
}
