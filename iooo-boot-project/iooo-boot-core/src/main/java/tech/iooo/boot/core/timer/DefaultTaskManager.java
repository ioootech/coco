package tech.iooo.boot.core.timer;

import java.util.concurrent.TimeUnit;
import tech.iooo.boot.core.threadpool.ThreadPoolConfig;
import tech.iooo.boot.core.threadpool.ThreadPools;

/**
 * @author 龙也
 * @date 2019/10/15 11:53 上午
 */
public class DefaultTaskManager extends AbstractTaskManager {

  @Override
  public void addTask(Runnable task, long delay, TimeUnit unit) {
    timer.newTimeout(timeout -> ThreadPools.limitedThreadPoolExecutor(
        ThreadPoolConfig.DEFAULT_CONFIG.setDaemon(false)
    ).execute(task), delay, unit);
  }
}
