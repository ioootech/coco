package tech.iooo.boot.cache;

import java.util.Set;

/**
 * Created on 2019-03-26 15:06
 *
 * @author <a href="mailto:yangkizhang@gmail.com?subject=iooo-boot">Ivan97</a>
 */
public class JedisCacheable<T> implements Cacheable<T> {

  @Override
  public T get(String key) {
    return JedisSentinelKit.get(key);
  }

  @Override
  public Set<String> keys(String pattern) {
    return JedisSentinelKit.keys(pattern);
  }

  @Override
  public boolean set(String key, T value) {
    return JedisSentinelKit.set(key, value);
  }

  @Override
  public boolean set(String key, T value, int timeout) {
    return JedisSentinelKit.set(key, value, timeout);
  }

  @Override
  public long del(String key) {
    return JedisSentinelKit.del(key);
  }

  @Override
  public Long ttl(String key) {
    return JedisSentinelKit.ttl(key);
  }

  @Override
  public boolean hasKey(String key) {
    return JedisSentinelKit.exists(key);
  }

  @Override
  public Long expire(String key, int seconds) {
    return JedisSentinelKit.expire(key, seconds);
  }

  @Override
  public void delAll(String pattern) {
    keys(pattern).forEach(this::del);
  }
}
