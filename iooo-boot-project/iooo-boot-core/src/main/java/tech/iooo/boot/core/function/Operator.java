package tech.iooo.boot.core.function;

import static java.util.Objects.requireNonNull;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import tech.iooo.boot.core.exception.WrappedException;

/**
 * @author 龙也
 * @date 2019/12/11 2:49 下午
 */
public interface Operator<T> extends UnaryOperator<T> {

  /**
   * Applies this operator to the given argument.
   *
   * @param t the operator argument
   * @return the operator result;
   */
  T operate(T t);

  static <T> Function<T, Optional<T>> lifted(final Operator<T> f) {
    return requireNonNull(f).lift();
  }

  static <T> Operator<T> unchecked(final Operator<T> f) {
    return requireNonNull(f).uncheck();
  }

  /**
   * Applies this operator to the given argument.
   *
   * @param t the operator argument
   * @return the operator result;
   */
  @Override
  default T apply(T t) {
    return operate(t);
  }

  /**
   * Returns a composed operator that first applies the {@code before} operator to its input, and then applies this operator to the result. If evaluation of either operator throws an exception, it is relayed to the caller of the composed
   * operator.
   *
   * @param before the operator to apply before this operator is applied
   * @return a composed operator that first applies the {@code before} operator and then applies this operator
   * @throws NullPointerException if before is null;
   * @see #andThen(Operator)
   */
  default Operator<T> compose(Operator<T> before) {
    Objects.requireNonNull(before);
    return t -> operate(before.operate(t));
  }

  /**
   * Returns a composed operator that first applies this operator to its input, and then applies the {@code after} operator to the result. If evaluation of either operator throws an exception, it is relayed to the caller of the composed
   * operator.
   *
   * @param after the operator to apply after this operator is applied
   * @return a composed operator that first applies this operator and then applies the {@code after} operator
   * @throws NullPointerException if after is null;
   * @see #compose(Operator)
   */
  default Operator<T> andThen(Operator<T> after) {
    Objects.requireNonNull(after);
    return t -> after.operate(operate(t));
  }

  default Operator<T> peek(Consumer<? super T> action) {
    Objects.requireNonNull(action);
    return t -> {
      action.accept(t);
      return operate(t);
    };
  }


  /**
   * Returns an operator that always returns its input argument.
   *
   * @param <T> the type of the input and output objects to the function
   * @return an operator that always returns its input argument;
   */
  static <T> Operator<T> identity() {
    return t -> t;
  }

  default Function<T, Optional<T>> lift() {
    return t -> {
      try {
        return Optional.ofNullable(operate(t));
      } catch (final Exception e) {
        return Optional.empty();
      }
    };
  }

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
