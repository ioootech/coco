package tech.iooo.boot.core.utils;

import java.util.Objects;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

/**
 * @author 龙也
 * @date 2019/10/12 12:03 下午
 */
public class SharedData<T> {

  private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
  private WriteLock writeLock = lock.writeLock();
  private ReadLock readLock = lock.readLock();

  private volatile T item;

  private SharedData(T item) {
    this.item = item;
  }

  public static <T> SharedData<T> init(T item) {
    if (Objects.isNull(item)) {
      return null;
    }
    return new SharedData<>(item);
  }

  /**
   * 获取当前值
   *
   * @return value
   */
  public T get() {
    try {
      readLock.lock();
      return this.item;
    } finally {
      readLock.unlock();
    }
  }

  /**
   * 设置新值，返回旧值
   *
   * @param item new value
   * @return old value
   */
  public T set(T item) {
    try {
      writeLock.lock();
      T oldValue = this.item;
      this.item = item;
      return oldValue;
    } finally {
      writeLock.unlock();
    }
  }

  @Override
  public String toString() {
    return item.toString();
  }
}
