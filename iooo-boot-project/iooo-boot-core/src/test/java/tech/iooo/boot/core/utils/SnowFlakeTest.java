package tech.iooo.boot.core.utils;

import org.junit.jupiter.api.Test;

/**
 * Created on 2018-11-23 23:48
 *
 * @author <a href="mailto:yangkizhang@gmail.com?subject=iooo-boot">Ivan97</a>
 */
class SnowFlakeTest {

  @Test
  void nextId() {
    SnowFlake snowFlake = new SnowFlake(5, 12);
    System.out.println(snowFlake.nextId());
  }
}
