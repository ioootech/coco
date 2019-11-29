package tech.iooo.boot.core.impl;

import tech.iooo.boot.core.Future;
import tech.iooo.boot.core.FutureFactory;
import tech.iooo.boot.core.Promise;

/**
 * @author ivan97
 */

public class FutureFactoryImpl implements FutureFactory {

  private static final SucceededFuture EMPTY = new SucceededFuture<>(null);

  @Override
  public <T> Promise<T> promise() {
    return new FutureImpl<>();
  }

  @Override
  public <T> Future<T> future() {
    return new FutureImpl<>();
  }

  @Override
  public <T> Future<T> succeededFuture() {
    @SuppressWarnings("unchecked")
    Future<T> fut = EMPTY;
    return fut;
  }

  @Override
  public <T> Future<T> succeededFuture(T result) {
    return new SucceededFuture<>(result);
  }

  @Override
  public <T> Future<T> failedFuture(Throwable t) {
    return new FailedFuture<>(t);
  }

  @Override
  public <T> Future<T> failureFuture(String failureMessage) {
    return new FailedFuture<>(failureMessage);
  }
}
