package tech.iooo.boot.core.exception;

import tech.iooo.boot.core.constants.SuppressTypeConstants;

/**
 * @author 龙也
 * @date 2019/12/12 6:19 下午
 */
public class SneakyThrowUtil {

  private SneakyThrowUtil() {
  }

  @SuppressWarnings(SuppressTypeConstants.UNCHECKED)
  public static <T extends Exception, R> R sneakyThrow(Exception t) throws T {
    throw (T) t;
  }
}
