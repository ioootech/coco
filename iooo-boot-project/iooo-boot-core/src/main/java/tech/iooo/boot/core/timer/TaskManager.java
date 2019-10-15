package tech.iooo.boot.core.timer;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.concurrent.TimeUnit;

/**
 * @author 龙也
 * @date 2019/10/15 5:37 下午
 */
public interface TaskManager {

  /**
   * 添加任务
   *
   * @param task 任务
   * @param delay 延迟时间
   * @param unit 单位
   */
  void addTask(Runnable task, long delay, TimeUnit unit);

  void addTask(Runnable task, ZonedDateTime executePoint);

  void addTask(Runnable task, Instant executePoint);
}
