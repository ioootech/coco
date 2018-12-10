package tech.iooo.boot.core.spring.cache;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;

/**
 * Created on 2018-12-10 14:02
 *
 * @author <a href="mailto:yangkizhang@gmail.com?subject=iooo-boot">Ivan97</a>
 */
public class CacheWrapper<T> {

  /**
   * 定时器域(时分秒..)
   */
  private ChronoUnit timeUnit;
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
  private LocalDateTime timer;

  public CacheWrapper(Cache cache) {
    this(cache.timeScale(), cache.timeInterval());
  }

  public CacheWrapper(ChronoUnit timeUnit, int amount) {
    this.timeUnit = timeUnit;
    this.amount = amount;
    resetTimer();
  }

  public void resetTimer() {
    timer = LocalDateTime.now(ZoneOffset.UTC);
    timer.plus(amount, timeUnit);
  }

  public boolean isTimeOut() {
    return LocalDateTime.now(ZoneOffset.UTC).isAfter(timer);
  }

  public T getCacheValue() {
    return cacheValue;
  }

  public void setCacheValue(T cacheValue) {
    this.cacheValue = cacheValue;
  }
}
