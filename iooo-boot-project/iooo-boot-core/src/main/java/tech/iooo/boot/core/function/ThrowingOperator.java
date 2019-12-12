package tech.iooo.boot.core.function;

import static java.util.Objects.requireNonNull;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import tech.iooo.boot.core.exception.SneakyThrowUtil;
import tech.iooo.boot.core.exception.WrappedException;

/**
 * @author 龙也
 * @date 2019/12/12 6:07 下午
 */
public interface ThrowingOperator<T, E extends Exception> extends ThrowingUnaryOperator<T, E> {

  /**
   * Applies this operator to the given argument.
   *
   * @param t the operator argument
   * @return the operator result;
   */
  T operate(T t) throws E;

  @Override
  default T apply(T t) throws E {
    return operate(t);
  }


  static <T> Function<T, Optional<T>> lifted(final ThrowingOperator<T, ?> f) {
    return requireNonNull(f).lift();
  }

  static <T> UnaryOperator<T> unchecked(final ThrowingOperator<T, ?> f) {
    return requireNonNull(f).uncheck();
  }

  static <V> UnaryOperator<V> sneaky(ThrowingOperator<V, ?> operator) {
    requireNonNull(operator);
    return v -> {
      try {
        return operator.operate(v);
      } catch (final Exception ex) {
        return SneakyThrowUtil.sneakyThrow(ex);
      }
    };
  }

  default ThrowingOperator<T, E> compose(final ThrowingOperator<T, ? extends E> before) {
    requireNonNull(before);
    return v -> operate(before.operate(v));
  }

  default ThrowingOperator<T, E> andThen(final ThrowingOperator<T, ? extends E> after) {
    requireNonNull(after);
    return t -> after.operate(operate(t));
  }

  @Override
  default Function<T, Optional<T>> lift() {
    return t -> {
      try {
        return Optional.ofNullable(operate(t));
      } catch (final Exception e) {
        return Optional.empty();
      }
    };
  }

  @Override
  default Operator<T> uncheck() {
    return t -> {
      try {
        return operate(t);
      } catch (final Exception e) {
        throw new WrappedException(e);
      }
    };
  }
}
