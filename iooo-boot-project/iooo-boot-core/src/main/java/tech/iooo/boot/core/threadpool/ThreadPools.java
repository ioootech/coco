package tech.iooo.boot.core.threadpool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import lombok.experimental.UtilityClass;
import tech.iooo.boot.core.threadpool.support.cached.CachedThreadPool;
import tech.iooo.boot.core.threadpool.support.eager.EagerThreadPool;
import tech.iooo.boot.core.threadpool.support.fixed.FixedThreadPool;
import tech.iooo.boot.core.threadpool.support.flexible.FlexibleThreadPool;
import tech.iooo.boot.core.threadpool.support.limited.LimitedThreadPool;

/**
 * Created on 2019-04-13 14:21
 *
 * @author <a href="mailto:yangkizhang@gmail.com?subject=iooo-boot">Ivan97</a>
 */
@UtilityClass
public class ThreadPools {

  public CachedThreadPool cachedThreadPool = new CachedThreadPool();
  public ExecutorService cachedThreadPoolExecutor = cachedThreadPool.executorService();

  public EagerThreadPool eagerThreadPool = new EagerThreadPool();
  public ExecutorService eagerThreadPoolExecutor = eagerThreadPool.executorService();

  public FixedThreadPool fixedThreadPool = new FixedThreadPool();
  public ExecutorService fixedThreadPoolExecutor = fixedThreadPool.executorService();

  public LimitedThreadPool limitedThreadPool = new LimitedThreadPool();
  public ExecutorService limitedThreadPoolExecutor = limitedThreadPool.executorService();

  public FlexibleThreadPool flexibleThreadPool = new FlexibleThreadPool();
  public ExecutorService flexibleThreadPoolExecutor = flexibleThreadPool.executorService();

  public ExecutorService singleThreadExecutor = fixedThreadPoolExecutor(ThreadPoolConfig.DEFAULT_CONFIG.setCores(1));

  public ScheduledExecutorService scheduledExecutor = ThreadPool.scheduledExecutorService(ThreadPoolConfig.DEFAULT_CONFIG);

  public ExecutorService cachedThreadPoolExecutor(ThreadPoolConfig config) {
    return cachedThreadPool.executorService(config);
  }

  public ExecutorService eagerThreadPoolExecutor(ThreadPoolConfig config) {
    return eagerThreadPool.executorService(config);
  }

  public ExecutorService fixedThreadPoolExecutor(ThreadPoolConfig config) {
    return fixedThreadPool.executorService(config);
  }

  public ExecutorService limitedThreadPoolExecutor(ThreadPoolConfig config) {
    return limitedThreadPool.executorService(config);
  }

  public ScheduledExecutorService scheduledExecutor(ThreadPoolConfig config) {
    return ThreadPool.scheduledExecutorService(config);
  }
}
