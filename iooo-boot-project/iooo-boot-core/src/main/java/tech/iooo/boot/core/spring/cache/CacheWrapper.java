package tech.iooo.boot.core.spring.cache;

import java.util.Calendar;

/**
 * Created on 2018-12-10 14:02
 *
 * @author <a href="mailto:yangkizhang@gmail.com?subject=iooo-boot">Ivan97</a>
 */
public class CacheWrapper<T> {

  /**
   * 定时器域(时分秒..)
   */
  private int field;
  /**
   * 定时器间隔
   */
  private int amount;
  /**
   * 缓存对象
   */
  private T cacheValue;
  /**
   * 定时器
   */
  private Calendar timer;

  public CacheWrapper(Cache cache) {
    this(cache.timeScale(), cache.timeInterval());
  }

  public CacheWrapper(int field, int amount) {
    this.field = field;
    this.amount = amount;
    resetTimer();
  }

  public void resetTimer() {
    timer = Calendar.getInstance();
    timer.add(field, amount);
  }

  public boolean isTimeOut() {
    return Calendar.getInstance().after(timer);
  }

  public T getCacheValue() {
    return cacheValue;
  }

  public void setCacheValue(T cacheValue) {
    this.cacheValue = cacheValue;
  }
}
