package tech.iooo.boot.cache;

import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Created on 2019-03-26 16:41
 *
 * @author <a href="mailto:yangkizhang@gmail.com?subject=iooo-boot">Ivan97</a>
 */
class CacheKitTest {

  private static JedisSentinelLifecycle jedisSentinelLifecycle;

  //
  @BeforeAll
  static void init() {
    jedisSentinelLifecycle = new JedisSentinelLifecycle(CacheType.Jedis, new RedisConfig());
    jedisSentinelLifecycle.start();
  }

  @AfterAll
  static void stop() {
    jedisSentinelLifecycle.stop();
  }

  @Test
  @SneakyThrows
  void test() {
    CacheKit.set("foo", "apple");
    System.out.println(CacheKit.get("foo"));
  }
}
