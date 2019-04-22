package tech.iooo.boot.core.utils;

import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created on 2019-04-22 11:00
 *
 * @author <a href="mailto:yangkizhang@gmail.com?subject=iooo-boot">Ivan97</a>
 */
public final class ConcurrentCache<K, V> {

  private final int size;

  private final Map<K, V> eden;

  private final Map<K, V> longTerm;

  public ConcurrentCache(int size) {
    this.size = size;
    this.eden = new ConcurrentHashMap<>(size);
    this.longTerm = new WeakHashMap<>(size);
  }

  public V get(K k) {
    V v = this.eden.get(k);
    if (v == null) {
      synchronized (longTerm) {
        v = this.longTerm.get(k);
      }
      if (v != null) {
        this.eden.put(k, v);
      }
    }
    return v;
  }

  public void put(K k, V v) {
    if (this.eden.size() >= size) {
      synchronized (longTerm) {
        this.longTerm.putAll(this.eden);
      }
      this.eden.clear();
    }
    this.eden.put(k, v);
  }
}
