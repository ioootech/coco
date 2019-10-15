package tech.iooo.boot.core.timer;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.concurrent.TimeUnit;

/**
 * @author 龙也
 * @date 2019/10/15 3:44 下午
 */
public abstract class AbstractTaskManager {

  protected HashedWheelTimer timer;

  /**
   * 添加任务
   *
   * @param task 任务
   * @param delay 延迟时间
   * @param unit 单位
   */
  abstract void addTask(Runnable task, long delay, TimeUnit unit);

  public void addTask(Runnable task, ZonedDateTime executePoint) {
    if (executePoint.isAfter(ZonedDateTime.now())) {
      addTask(task, executePoint.toInstant().toEpochMilli() - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
    }
  }

  public void addTask(Runnable task, Instant executePoint) {
    if (executePoint.isAfter(Instant.now())) {
      addTask(task, executePoint.toEpochMilli() - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
    }
  }

  public void start() {
    timer.start();
  }
}
