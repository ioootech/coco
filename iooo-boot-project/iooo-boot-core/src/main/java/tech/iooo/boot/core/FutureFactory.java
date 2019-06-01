package tech.iooo.boot.core;

import tech.iooo.boot.core.utils.ServiceHelper;

/**
 * Created on 2019-03-28 09:17
 *
 * @author <a href="mailto:yangkizhang@gmail.com?subject=iooo-boot">Ivan97</a>
 */
public interface FutureFactory {

  static FutureFactory instance() {
    return ServiceHelper.loadFactory(FutureFactory.class);
  }

  <T> Future<T> future();

  <T> Future<T> succeededFuture();

  <T> Future<T> succeededFuture(T result);

  <T> Future<T> failedFuture(Throwable t);

  <T> Future<T> failureFuture(String failureMessage);
}
