package tech.iooo.boot.core.logging;

import java.io.IOException;
import java.io.OutputStream;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.event.Level;
import tech.iooo.boot.core.constants.SystemProperties;

/**
 * Created on 2018-12-24 14:56
 *
 * @author <a href="mailto:yangkizhang@gmail.com?subject=iooo-java8-in-action">Ivan97</a>
 */
@Data
public class LoggingOutputStream extends OutputStream {

  private Logger logger;

  private Level level;

  private String mem;

  public LoggingOutputStream(Logger logger, Level level) {
    setLogger(logger);
    setLevel(level);
    mem = "";
  }


  @Override
  public void write(int b) throws IOException {
    byte[] bytes = new byte[1];
    bytes[0] = (byte) (b & 0xff);
    mem = mem + new String(bytes);
    if (mem.endsWith(SystemProperties.LINE_SEPARATOR)) {
      mem = mem.substring(0, mem.length() - 1);
      flush();
    }
  }

  @Override
  public void flush() throws IOException {
    switch (level) {
      case INFO:
        if (logger.isInfoEnabled()) {
          logger.info(mem);
        }
        break;
      case WARN:
        logger.warn(mem);
        break;
      case DEBUG:
        if (logger.isDebugEnabled()) {
          logger.debug(mem);
        }
        break;
      case ERROR:
        logger.error(mem);
        break;
      case TRACE:
        if (logger.isTraceEnabled()) {
          logger.trace(mem);
        }
        break;
      default:
        break;
    }
    mem = "";
  }
}
