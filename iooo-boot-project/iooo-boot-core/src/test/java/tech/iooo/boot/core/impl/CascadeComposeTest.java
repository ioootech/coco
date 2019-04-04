package tech.iooo.boot.core.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;
import java.util.function.Function;
import org.junit.jupiter.api.Test;

/**
 * Created on 2019-03-28 22:31
 *
 * @author <a href="mailto:yangkizhang@gmail.com?subject=iooo-boot">Ivan97</a>
 */
class CascadeComposeTest {

  @Test
  void test() {
    CascadeCompose<Integer> cascadeCompose = x -> y -> z -> x.apply(y.apply(z));
    Function<Integer, Integer> triple = x -> x * 3;
    Function<Integer, Integer> square = x -> x * x;
    assertEquals(Optional.ofNullable(cascadeCompose.apply(triple).apply(square).apply(2)), Optional.of(12));
    assertEquals(Optional.ofNullable(cascadeCompose.apply(square).apply(triple).apply(2)), Optional.of(36));
  }
}
