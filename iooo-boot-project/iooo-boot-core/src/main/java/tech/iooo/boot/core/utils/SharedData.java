package tech.iooo.boot.core.utils;

import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

/**
 * @author 龙也
 * @date 2019/10/12 12:03 下午
 */
public class SharedData<T> {

  private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
  private ReadLock readLock = lock.readLock();
  private WriteLock writeLock = lock.writeLock();

  private volatile T item;

  private SharedData(T item) {
    this.item = item;
  }

  public static <T> SharedData<T> init(T item) {
    return new SharedData<>(item);
  }

  public T get() {
    try {
      readLock.lock();
      return this.item;
    } finally {
      readLock.unlock();
    }
  }

  public void set(T item) {
    try {
      writeLock.lock();
      this.item = item;
    } finally {
      writeLock.unlock();
    }
  }
}
