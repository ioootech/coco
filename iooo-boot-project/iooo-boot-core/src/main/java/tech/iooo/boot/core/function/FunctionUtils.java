package tech.iooo.boot.core.function;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import javax.annotation.Nonnull;

/**
 * @author 龙也
 * @date 2019/12/6 2:30 下午
 */
public class FunctionUtils {

  public static <T> void checkThenConsume(T item, @Nonnull Predicate<T> predicate, Consumer<T> consumer) {
    if (predicate.test(item)) {
      consumer.accept(item);
    }
  }

  public static <T, R> R checkAndThen(T item, @Nonnull Predicate<T> predicate, Function<T, R> function) {
    if (predicate.test(item)) {
      return function.apply(item);
    } else {
      return null;
    }
  }
}
