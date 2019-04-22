package tech.iooo.boot.core.threadpool.support.flexible;

import java.util.concurrent.ExecutorService;
import lombok.experimental.Delegate;
import tech.iooo.boot.core.threadpool.ThreadPool;
import tech.iooo.boot.core.threadpool.ThreadPoolConfig;

/**
 * Created on 2019-04-22 16:33
 *
 * @author <a href="mailto:yangkizhang@gmail.com?subject=iooo-boot">Ivan97</a>
 */
public class FlexibleThreadPool implements ThreadPool {

  @Delegate
  private ThreadPoolConfig config = ThreadPoolConfig.DEFAULT_CONFIG;

  public ExecutorService executorService() {
    return executorService(config);
  }
}
