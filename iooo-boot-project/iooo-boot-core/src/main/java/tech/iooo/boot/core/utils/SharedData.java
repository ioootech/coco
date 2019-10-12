package tech.iooo.boot.core.utils;

import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

/**
 * @author 龙也
 * @date 2019/10/12 12:03 下午
 */
public class SharedData<T> {

  private static ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
  private static ReadLock readLock = lock.readLock();
  private static WriteLock writeLock = lock.writeLock();

  private volatile T item;

  private SharedData(T item) {
    this.item = item;
  }

  public static <T> SharedData<T> init(T item) {
    try {
      writeLock.lock();
      return new SharedData<>(item);
    } finally {
      writeLock.unlock();
    }
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
