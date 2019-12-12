package tech.iooo.boot.core.function;

import static java.util.Objects.requireNonNull;

import java.util.Optional;
import java.util.function.Function;
import tech.iooo.boot.core.exception.SneakyThrowUtil;
import tech.iooo.boot.core.exception.WrappedException;

/**
 * @author 龙也
 * @date 2019/12/12 8:05 下午
 */
@FunctionalInterface
interface InnerThrowingFunction<T, R, E extends Exception> {

  R apply(T arg) throws E;

  /**
   * @return a Function that returns the result of the given function as an Optional instance. In case of a failure, empty Optional is returned
   */
  static <T, R> Function<T, Optional<R>> lifted(final InnerThrowingFunction<T, R, ?> f) {
    return requireNonNull(f).lift();
  }

  static <T, R> Function<T, R> unchecked(final InnerThrowingFunction<T, R, ?> f) {
    return requireNonNull(f).uncheck();
  }

  static <T1, R> Function<T1, R> sneaky(InnerThrowingFunction<? super T1, ? extends R, ?> function) {
    requireNonNull(function);
    return t -> {
      try {
        return function.apply(t);
      } catch (final Exception ex) {
        return SneakyThrowUtil.sneakyThrow(ex);
      }
    };
  }

  default <V> InnerThrowingFunction<V, R, E> compose(final InnerThrowingFunction<? super V, ? extends T, ? extends E> before) {
    requireNonNull(before);
    return v -> apply(before.apply(v));
  }

  default <V> InnerThrowingFunction<T, V, E> andThen(final InnerThrowingFunction<? super R, ? extends V, ? extends E> after) {
    requireNonNull(after);
    return t -> after.apply(apply(t));
  }

  default Function<T, Optional<R>> lift() {
    return t -> {
      try {
        return Optional.ofNullable(apply(t));
      } catch (final Exception e) {
        return Optional.empty();
      }
    };
  }

  default Function<T, R> uncheck() {
    return t -> {
      try {
        return apply(t);
      } catch (final Exception e) {
        throw new WrappedException(e);
      }
    };
  }
}
