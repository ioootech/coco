package tech.iooo.boot.core.function;

import java.util.Objects;
import java.util.function.Function;

/**
 * @author 龙也
 * @date 2019/12/11 2:49 下午
 */
public interface Operator<T> {

  /**
   * 操作对象
   *
   * @param t input
   * @return output
   */
  T operate(T t);

  /**
   * 后续操作
   *
   * @param after after
   * @return Operator
   */
  default Operator<T> andThen(Operator<T> after) {
    Objects.requireNonNull(after);
    return t -> after.operate(operate(t));
  }

  /**
   * 后续操作
   *
   * @param after after
   * @return Operator
   */
  default Operator<T> andThenWithFunction(Function<T, T> after) {
    Objects.requireNonNull(after);
    return t -> after.apply(operate(t));
  }
}
