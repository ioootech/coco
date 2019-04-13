package tech.iooo.boot.core.threadpool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import lombok.experimental.UtilityClass;
import tech.iooo.boot.core.threadpool.support.cached.CachedThreadPool;
import tech.iooo.boot.core.threadpool.support.eager.EagerThreadPool;
import tech.iooo.boot.core.threadpool.support.fixed.FixedThreadPool;
import tech.iooo.boot.core.threadpool.support.limited.LimitedThreadPool;

/**
 * Created on 2019-04-13 14:21
 *
 * @author <a href="mailto:yangkizhang@gmail.com?subject=iooo-boot">Ivan97</a>
 */
@UtilityClass
public class ThreadPools {

  public CachedThreadPool cachedThreadPool = new CachedThreadPool();
  public ExecutorService cachedThreadPoolExecutorService = cachedThreadPool.executorService();
  public EagerThreadPool eagerThreadPool = new EagerThreadPool();
  public ExecutorService eagerThreadPoolExecutorService = eagerThreadPool.executorService();
  public FixedThreadPool fixedThreadPool = new FixedThreadPool();
  public ExecutorService fixedThreadPoolExecutorService = fixedThreadPool.executorService();
  public LimitedThreadPool limitedThreadPool = new LimitedThreadPool();
  public ExecutorService limitedThreadPoolExecutorService = limitedThreadPool.executorService();
  public ScheduledExecutorService scheduledExecutorService = ThreadPool.scheduledExecutorService(ThreadPoolConfig.DEFAULT_CONFIG);

  public ExecutorService cachedThreadPoolExecutorService(ThreadPoolConfig config) {
    return cachedThreadPool.executorService(config);
  }

  public ExecutorService eagerThreadPoolExecutorService(ThreadPoolConfig config) {
    return eagerThreadPool.executorService(config);
  }

  public ExecutorService fixedThreadPoolExecutorService(ThreadPoolConfig config) {
    return fixedThreadPool.executorService(config);
  }

  public ExecutorService limitedThreadPoolExecutorService(ThreadPoolConfig config) {
    return limitedThreadPool.executorService(config);
  }

  public ScheduledExecutorService scheduledExecutorService(ThreadPoolConfig config) {
    return ThreadPool.scheduledExecutorService(config);
  }
}
