package tech.iooo.boot.core.timer;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.concurrent.TimeUnit;

/**
 * @author 龙也
 * @date 2019/10/15 3:44 下午
 */
public abstract class AbstractTaskManager implements TaskManager {

  protected static HashedWheelTimer timer;

  @Override
  public void addTask(Runnable task, ZonedDateTime executePoint) {
    if (executePoint.isAfter(ZonedDateTime.now())) {
      addTask(task, executePoint.toInstant().toEpochMilli() - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
    }
  }

  @Override
  public void addTask(Runnable task, Instant executePoint) {
    if (executePoint.isAfter(Instant.now())) {
      addTask(task, executePoint.toEpochMilli() - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
    }
  }

  protected void start() {
    timer.start();
  }
}
