package tech.iooo.boot.core.threadpool.support.flexible;

import java.util.concurrent.ExecutorService;
import tech.iooo.boot.core.threadpool.ThreadPool;
import tech.iooo.boot.core.threadpool.ThreadPoolConfig;

/**
 * Created on 2019-04-22 16:33
 *
 * @author <a href="mailto:yangkizhang@gmail.com?subject=iooo-boot">Ivan97</a>
 */
public class FlexibleThreadPool implements ThreadPool {

  private ThreadPoolConfig config = ThreadPoolConfig.DEFAULT_CONFIG;

  public ExecutorService executorService() {
    return executorService(config);
  }

  public FlexibleThreadPool withCores(int cores) {
    this.config.setCores(cores);
    return this;
  }

  public FlexibleThreadPool withNamePrefix(String namePrefix) {
    this.config.setNamePrefix(namePrefix);
    return this;
  }

  public FlexibleThreadPool withThreads(int threads) {
    this.config.setThreads(threads);
    return this;
  }

  public FlexibleThreadPool withQueues(int queues) {
    this.config.setQueues(queues);
    return this;
  }

  public FlexibleThreadPool withAlive(long alive) {
    this.config.setAlive(alive);
    return this;
  }

  public FlexibleThreadPool withDaemon(boolean daemon) {
    this.config.setDaemon(daemon);
    return this;
  }
}
