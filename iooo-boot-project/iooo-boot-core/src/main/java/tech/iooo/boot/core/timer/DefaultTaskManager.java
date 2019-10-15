package tech.iooo.boot.core.timer;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import java.util.concurrent.TimeUnit;
import tech.iooo.boot.core.threadpool.ThreadPoolConfig;
import tech.iooo.boot.core.threadpool.ThreadPools;

/**
 * @author 龙也
 * @date 2019/10/15 11:53 上午
 */
public class DefaultTaskManager extends AbstractTaskManager {

  private DefaultTaskManager() {
  }

  public static DefaultTaskManager init() {
    timer = new HashedWheelTimer(new ThreadFactoryBuilder()
        .setDaemon(false)
        .setNameFormat("i-wheel-timer-%d")
        .build());
    DefaultTaskManager manager = new DefaultTaskManager();
    manager.start();
    return manager;
  }

  public static DefaultTaskManager init(HashedWheelTimer hashedWheelTimer) {
    timer = hashedWheelTimer;
    DefaultTaskManager manager = new DefaultTaskManager();
    manager.start();
    return manager;
  }

  @Override
  public void addTask(Runnable task, long delay, TimeUnit unit) {
    timer.newTimeout(timeout -> ThreadPools.limitedThreadPoolExecutor(
        ThreadPoolConfig.DEFAULT_CONFIG.setDaemon(false)
    ).execute(task), delay, unit);
  }
}
