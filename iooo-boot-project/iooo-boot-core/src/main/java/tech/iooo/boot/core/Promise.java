package tech.iooo.boot.core;

import static tech.iooo.boot.core.Future.factory;

/**
 * @author 龙也
 * @date 2019/11/29 10:48 上午
 */
public interface Promise<T> extends Handler<AsyncResult<T>> {

  /**
   * Create a promise that hasn't completed yet
   *
   * @param <T> the result type
   * @return the promise
   */
  static <T> Promise<T> promise() {
    return factory.promise();
  }

  /**
   * Succeed or fail this promise with the {@link AsyncResult} event.
   *
   * @param asyncResult the async result to handle
   */
  @Override
  void handle(AsyncResult<T> asyncResult);

  /**
   * Set the result. Any handler will be called, if there is one, and the promise will be marked as completed.
   * <p/>
   * Any handler set on the associated promise will be called.
   *
   * @param result the result
   * @throws IllegalStateException when the promise is already completed
   */
  void complete(T result);

  /**
   * Calls {@code complete(null)}
   *
   * @throws IllegalStateException when the promise is already completed
   */
  void complete();

  /**
   * Set the failure. Any handler will be called, if there is one, and the future will be marked as completed.
   *
   * @param cause the failure cause
   * @throws IllegalStateException when the promise is already completed
   */
  void fail(Throwable cause);

  /**
   * Calls {@link #fail(Throwable)} with the {@code message}.
   *
   * @param message the failure message
   * @throws IllegalStateException when the promise is already completed
   */
  void fail(String message);

  /**
   * Like {@link #complete(Object)} but returns {@code false} when the promise is already completed instead of throwing an {@link IllegalStateException}, it returns {@code true} otherwise.
   *
   * @param result the result
   * @return {@code false} when the future is already completed
   */
  boolean tryComplete(T result);

  /**
   * Calls {@code tryComplete(null)}.
   *
   * @return {@code false} when the future is already completed
   */
  boolean tryComplete();

  /**
   * Like {@link #fail(Throwable)} but returns {@code false} when the promise is already completed instead of throwing an {@link IllegalStateException}, it returns {@code true} otherwise.
   *
   * @param cause the failure cause
   * @return {@code false} when the future is already completed
   */
  boolean tryFail(Throwable cause);

  /**
   * Calls {@link #fail(Throwable)} with the {@code message}.
   *
   * @param message the failure message
   * @return false when the future is already completed
   */
  boolean tryFail(String message);

  /**
   * @return the {@link Future} associated with this promise, it can be used to be aware of the promise completion
   */
  Future<T> future();
}

