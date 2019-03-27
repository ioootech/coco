package tech.iooo.boot.cache;

import java.util.Set;
import tech.iooo.boot.core.utils.LRUCache;

/**
 * Created on 2019-03-26 15:04
 *
 * @author <a href="mailto:yangkizhang@gmail.com?subject=iooo-boot">Ivan97</a>
 */
public class LRUCacheable<T> implements Cacheable<T> {

  private LRUCache<String, T> lruCache = new LRUCache<>();

  public LRUCacheable() {
  }

  @Override
  public long del(String key) {
    this.lruCache.remove(key);
    return 1L;
  }

  @Override
  public Long ttl(String key) {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean hasKey(String key) {
    return lruCache.containsKey(key);
  }

  @Override
  public Long expire(String key, int seconds) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void delAll(String pattern) {
    this.lruCache.clear();
  }

  @Override
  public T get(String key) {
    return this.lruCache.get(key);
  }

  @Override
  public Set<String> keys(String pattern) {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean set(String key, T value) {
    this.lruCache.put(key, value);
    return true;
  }

  @Override
  public boolean set(String key, T value, int seconds) {
    return this.set(key, value);
  }
}
