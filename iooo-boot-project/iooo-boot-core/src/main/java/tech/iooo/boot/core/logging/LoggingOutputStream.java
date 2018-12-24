package tech.iooo.boot.core.logging;

import java.io.IOException;
import java.io.OutputStream;
import org.slf4j.Logger;
import org.slf4j.event.Level;
import tech.iooo.boot.core.constants.SystemProperties;

/**
 * Created on 2018-12-24 14:56
 *
 * @author <a href="mailto:yangkizhang@gmail.com?subject=iooo-java8-in-action">Ivan97</a>
 */
public class LoggingOutputStream extends OutputStream {


  /**
   * Indicates stream state.
   */
  private boolean hasBeenClosed = false;

  /**
   * Internal buffer where data is stored.
   */
  private String mem;

  /**
   * Remembers the size of the buffer.
   */
  private int curBufLength;

  /**
   * The logger to write to.
   */
  private Logger logger;

  /**
   * The log level.
   */
  private Level level;

  /**
   * Creates the Logging instance to flush to the given logger.
   *
   * @param logger the Logger to write to
   * @param level the log level
   * @throws IllegalArgumentException in case if one of arguments is  null.
   */
  public LoggingOutputStream(Logger logger, Level level) throws IllegalArgumentException {
    this.logger = logger;
    this.level = level;
    mem = "";
  }


  /**
   * Writes the specified byte to this output stream.
   *
   * @param b the byte to write
   * @throws IOException if an I/O error occurs.
   */
  @Override
  public void write(int b) throws IOException {
    if (hasBeenClosed) {
      throw new IOException("The stream has been closed.");
    }
    byte[] bytes = new byte[1];
    bytes[0] = (byte) (b & 0xff);
    mem = mem + new String(bytes);

    if (mem.endsWith(SystemProperties.LINE_SEPARATOR)) {
      mem = mem.substring(0, mem.length() - 1);
      flush();
    }
  }

  /**
   * Flushes this output stream and forces any buffered output bytes to be written out.
   */
  @Override
  public void flush() {
    LoggingUtils.log(logger, level, mem);
    mem = "";
  }

  /**
   * Closes this output stream and releases any system resources associated with this stream.
   */
  @Override
  public void close() {
    flush();
    hasBeenClosed = true;
  }
}
