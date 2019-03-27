package tech.iooo.boot.cache;

import java.util.Set;

/**
 * Created on 2019-03-26 15:04
 *
 * @author <a href="mailto:yangkizhang@gmail.com?subject=iooo-boot">Ivan97</a>
 */
public interface Cacheable<T> {

  T get(String key);

  Set<String> keys(String pattern);

  boolean set(String key, T value);

  boolean set(String key, T value, int timeout);

  long del(String key);

  Long ttl(String key);

  boolean hasKey(String key);

  Long expire(String key, int seconds);

  void delAll(String pattern);
}
