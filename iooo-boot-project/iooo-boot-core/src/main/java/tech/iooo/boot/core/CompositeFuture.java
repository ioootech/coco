package tech.iooo.boot.core;

import java.util.ArrayList;
import java.util.List;
import tech.iooo.boot.core.impl.CompositeFutureImpl;

/**
 * Created on 2019-03-28 10:22
 *
 * @author <a href="mailto:yangkizhang@gmail.com?subject=iooo-boot">Ivan97</a>
 */
public interface CompositeFuture extends Future<CompositeFuture> {

  /**
   * Return a composite future, succeeded when all futures are succeeded, failed when any future is failed.
   * <p/>
   * The returned future fails as soon as one of {@code f1} or {@code f2} fails.
   *
   * @param f1 future
   * @param f2 future
   * @return the composite future
   */
  static <T1, T2> CompositeFuture all(Future<T1> f1, Future<T2> f2) {
    return CompositeFutureImpl.all(f1, f2);
  }

  /**
   * Like {@link #all(Future, Future)} but with 3 futures.
   */
  static <T1, T2, T3> CompositeFuture all(Future<T1> f1, Future<T2> f2, Future<T3> f3) {
    return CompositeFutureImpl.all(f1, f2, f3);
  }

  /**
   * Like {@link #all(Future, Future)} but with 4 futures.
   */
  static <T1, T2, T3, T4> CompositeFuture all(Future<T1> f1, Future<T2> f2, Future<T3> f3, Future<T4> f4) {
    return CompositeFutureImpl.all(f1, f2, f3, f4);
  }

  /**
   * Like {@link #all(Future, Future)} but with 5 futures.
   */
  static <T1, T2, T3, T4, T5> CompositeFuture all(Future<T1> f1, Future<T2> f2, Future<T3> f3, Future<T4> f4, Future<T5> f5) {
    return CompositeFutureImpl.all(f1, f2, f3, f4, f5);
  }

  /**
   * Like {@link #all(Future, Future)} but with 6 futures.
   */
  static <T1, T2, T3, T4, T5, T6> CompositeFuture all(Future<T1> f1, Future<T2> f2, Future<T3> f3, Future<T4> f4, Future<T5> f5, Future<T6> f6) {
    return CompositeFutureImpl.all(f1, f2, f3, f4, f5, f6);
  }

  /**
   * Like {@link #all(Future, Future)} but with a list of futures.<p>
   *
   * When the list is empty, the returned future will be already completed.
   */
  static CompositeFuture all(List<Future> futures) {
    return CompositeFutureImpl.all(futures.toArray(new Future[futures.size()]));
  }

  /**
   * Return a composite future, succeeded when any futures is succeeded, failed when all futures are failed.
   * <p/>
   * The returned future succeeds as soon as one of {@code f1} or {@code f2} succeeds.
   *
   * @param f1 future
   * @param f2 future
   * @return the composite future
   */
  static <T1, T2> CompositeFuture any(Future<T1> f1, Future<T2> f2) {
    return CompositeFutureImpl.any(f1, f2);
  }

  /**
   * Like {@link #any(Future, Future)} but with 3 futures.
   */
  static <T1, T2, T3> CompositeFuture any(Future<T1> f1, Future<T2> f2, Future<T3> f3) {
    return CompositeFutureImpl.any(f1, f2, f3);
  }

  /**
   * Like {@link #any(Future, Future)} but with 4 futures.
   */
  static <T1, T2, T3, T4> CompositeFuture any(Future<T1> f1, Future<T2> f2, Future<T3> f3, Future<T4> f4) {
    return CompositeFutureImpl.any(f1, f2, f3, f4);
  }

  /**
   * Like {@link #any(Future, Future)} but with 5 futures.
   */
  static <T1, T2, T3, T4, T5> CompositeFuture any(Future<T1> f1, Future<T2> f2, Future<T3> f3, Future<T4> f4, Future<T5> f5) {
    return CompositeFutureImpl.any(f1, f2, f3, f4, f5);
  }

  /**
   * Like {@link #any(Future, Future)} but with 6 futures.
   */
  static <T1, T2, T3, T4, T5, T6> CompositeFuture any(Future<T1> f1, Future<T2> f2, Future<T3> f3, Future<T4> f4, Future<T5> f5, Future<T6> f6) {
    return CompositeFutureImpl.any(f1, f2, f3, f4, f5, f6);
  }

  /**
   * Like {@link #any(Future, Future)} but with a list of futures.<p>
   *
   * When the list is empty, the returned future will be already completed.
   */
  static CompositeFuture any(List<Future> futures) {
    return CompositeFutureImpl.any(futures.toArray(new Future[futures.size()]));
  }

  /**
   * Return a composite future, succeeded when all futures are succeeded, failed when any future is failed.
   * <p/>
   * It always wait until all its futures are completed and will not fail as soon as one of {@code f1} or {@code f2} fails.
   *
   * @param f1 future
   * @param f2 future
   * @return the composite future
   */
  static <T1, T2> CompositeFuture join(Future<T1> f1, Future<T2> f2) {
    return CompositeFutureImpl.join(f1, f2);
  }

  /**
   * Like {@link #join(Future, Future)} but with 3 futures.
   */
  static <T1, T2, T3> CompositeFuture join(Future<T1> f1, Future<T2> f2, Future<T3> f3) {
    return CompositeFutureImpl.join(f1, f2, f3);
  }

  /**
   * Like {@link #join(Future, Future)} but with 4 futures.
   */
  static <T1, T2, T3, T4> CompositeFuture join(Future<T1> f1, Future<T2> f2, Future<T3> f3, Future<T4> f4) {
    return CompositeFutureImpl.join(f1, f2, f3, f4);
  }

  /**
   * Like {@link #join(Future, Future)} but with 5 futures.
   */
  static <T1, T2, T3, T4, T5> CompositeFuture join(Future<T1> f1, Future<T2> f2, Future<T3> f3, Future<T4> f4, Future<T5> f5) {
    return CompositeFutureImpl.join(f1, f2, f3, f4, f5);
  }

  /**
   * Like {@link #join(Future, Future)} but with 6 futures.
   */
  static <T1, T2, T3, T4, T5, T6> CompositeFuture join(Future<T1> f1, Future<T2> f2, Future<T3> f3, Future<T4> f4, Future<T5> f5, Future<T6> f6) {
    return CompositeFutureImpl.join(f1, f2, f3, f4, f5, f6);
  }

  /**
   * Like {@link #join(Future, Future)} but with a list of futures.<p>
   *
   * When the list is empty, the returned future will be already completed.
   */
  static CompositeFuture join(List<Future> futures) {
    return CompositeFutureImpl.join(futures.toArray(new Future[futures.size()]));
  }

  @Override
  CompositeFuture setHandler(Handler<AsyncResult<CompositeFuture>> handler);

  @Override
  default CompositeFuture onComplete(Handler<AsyncResult<CompositeFuture>> handler) {
    return setHandler(handler);
  }

  @Override
  default CompositeFuture onSuccess(Handler<CompositeFuture> handler) {
    Future.super.onSuccess(handler);
    return this;
  }

  @Override
  default CompositeFuture onFailure(Handler<Throwable> handler) {
    Future.super.onFailure(handler);
    return this;
  }

  /**
   * Set this instance as result. Any handler will be called, if there is one, and the future will be marked as completed.
   */
  @Override
  void complete();

  /**
   * Try to set this instance as result. When it happens, any handler will be called, if there is one, and the future will be marked as completed.
   *
   * @return false when the future is already completed
   */
  @Override
  boolean tryComplete();

  /**
   * Returns a cause of a wrapped future
   *
   * @param index the wrapped future index
   */
  Throwable cause(int index);

  /**
   * Returns true if a wrapped future is succeeded
   *
   * @param index the wrapped future index
   */
  boolean succeeded(int index);

  /**
   * Returns true if a wrapped future is failed
   *
   * @param index the wrapped future index
   */
  boolean failed(int index);

  /**
   * Returns true if a wrapped future is completed
   *
   * @param index the wrapped future index
   */
  boolean isComplete(int index);

  /**
   * Returns the result of a wrapped future
   *
   * @param index the wrapped future index
   */
  <T> T resultAt(int index);

  /**
   * @return the number of wrapped future
   */
  int size();

  /**
   * @return a list of the current completed values. If one future is not yet resolved or is failed, {@code} null will be used
   */
  default <T> List<T> list() {
    int size = size();
    ArrayList<T> list = new ArrayList<>(size);
    for (int index = 0; index < size; index++) {
      list.add(resultAt(index));
    }
    return list;
  }
}

