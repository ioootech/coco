package tech.iooo.boot.core.exception;

/**
 * @author 龙也
 * @date 2019/12/12 6:12 下午
 */
public class WrappedException extends RuntimeException {

  public WrappedException(Throwable cause) {
    super(cause.getMessage(), cause);
  }
}
