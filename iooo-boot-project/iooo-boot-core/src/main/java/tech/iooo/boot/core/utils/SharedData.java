package tech.iooo.boot.core.utils;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @author 龙也
 * @date 2019/10/12 12:03 下午
 */
public class SharedData<T> {

  private ReentrantLock lock = new ReentrantLock();

  private volatile T item;

  private SharedData(T item) {
    this.item = item;
  }

  public static <T> SharedData<T> init(T item) {
    return new SharedData<>(item);
  }

  public T get() {
    try {
      lock.lock();
      return this.item;
    } finally {
      lock.unlock();
    }
  }

  public void set(T item) {
    try {
      lock.lock();
      this.item = item;
    } finally {
      lock.unlock();
    }
  }
}
