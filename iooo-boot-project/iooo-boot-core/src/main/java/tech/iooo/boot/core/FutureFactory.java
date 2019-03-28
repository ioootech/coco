package tech.iooo.boot.core;

/**
 * Created on 2019-03-28 09:17
 *
 * @author <a href="mailto:yangkizhang@gmail.com?subject=iooo-boot">Ivan97</a>
 */
public interface FutureFactory {

  <T> Future<T> future();

  <T> Future<T> succeededFuture();

  <T> Future<T> succeededFuture(T result);

  <T> Future<T> failedFuture(Throwable t);

  <T> Future<T> failureFuture(String failureMessage);
}
