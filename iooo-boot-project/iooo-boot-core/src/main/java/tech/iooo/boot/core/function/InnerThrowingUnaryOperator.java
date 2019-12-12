package tech.iooo.boot.core.function;

import static java.util.Objects.requireNonNull;

import java.util.Objects;
import java.util.function.UnaryOperator;
import tech.iooo.boot.core.exception.SneakyThrowUtil;
import tech.iooo.boot.core.exception.WrappedException;

/**
 * @author 龙也
 * @date 2019/12/12 8:05 下午
 */
@FunctionalInterface
interface InnerThrowingUnaryOperator<T, E extends Exception> extends InnerThrowingFunction<T, T, E> {

  static <T> UnaryOperator<T> unchecked(InnerThrowingUnaryOperator<T, ?> operator) {
    return requireNonNull(operator).uncheck();
  }

  /**
   * Returns a new UnaryOperator instance which rethrows the checked exception using the Sneaky Throws pattern
   *
   * @return UnaryOperator instance that rethrows the checked exception using the Sneaky Throws pattern
   */
  static <T> UnaryOperator<T> sneaky(InnerThrowingUnaryOperator<T, ?> operator) {
    Objects.requireNonNull(operator);
    return t -> {
      try {
        return operator.apply(t);
      } catch (Exception e) {
        return SneakyThrowUtil.sneakyThrow(e);
      }
    };
  }

  /**
   * Returns a new UnaryOperator instance which wraps thrown checked exception instance into a RuntimeException
   */
  @Override
  default Operator<T> uncheck() {
    return t -> {
      try {
        return apply(t);
      } catch (final Exception e) {
        throw new WrappedException(e);
      }
    };
  }
}
