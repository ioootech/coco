package tech.iooo.boot.cache;

import java.util.Set;
import lombok.experimental.UtilityClass;

/**
 * Created on 2019-03-26 14:55
 *
 * @author <a href="mailto:yangkizhang@gmail.com?subject=iooo-boot">Ivan97</a>
 */
@UtilityClass
public class CacheKit {

  private static Cacheable cacheable = new LRUCacheable();

  public static void init(Cacheable initCacheable) {
    cacheable = initCacheable;
  }

  public static Object get(String key) {
    return cacheable.get(key);
  }

  public static boolean set(String key, Object value) {
    return cacheable.set(key, value);
  }

  public static boolean set(String key, Object value, int seconds) {
    return cacheable.set(key, value, seconds);
  }

  public static long del(String key) {
    return cacheable.del(key);
  }

  public static void delAll(String pattern) {
    cacheable.delAll(pattern);
  }

  public static long ttl(String key) {
    return cacheable.ttl(key);
  }

  public static long expire(String key, int seconds) {
    return cacheable.expire(key, seconds);
  }

  public static Set<String> keys(String pattern) {
    return cacheable.keys(pattern);
  }

}
