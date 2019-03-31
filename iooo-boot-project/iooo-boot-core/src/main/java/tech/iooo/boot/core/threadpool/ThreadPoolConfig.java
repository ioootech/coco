package tech.iooo.boot.core.threadpool;

import lombok.Builder;
import lombok.Data;
import tech.iooo.boot.core.constants.Constants;

/**
 * Created on 2019-03-20 13:52
 *
 * @author <a href="mailto:yangkizhang@gmail.com?subject=iooo-boot">Ivan97</a>
 */
@Data
@Builder
public class ThreadPoolConfig {

  public static final ThreadPoolConfig DEFAULT_CONFIG = new ThreadPoolConfigBuilder()
      .namePrefix(Constants.DEFAULT_THREAD_NAME_PREFIX)
      .cores(Constants.DEFAULT_CORE_THREADS)
      .threads(Constants.DEFAULT_THREADS)
      .queues(Constants.DEFAULT_QUEUES)
      .alive(Constants.DEFAULT_ALIVE)
      .daemon(Constants.DEFAULT_THREAD_DAEMON)
      .build();

  private String namePrefix = Constants.DEFAULT_THREAD_NAME_PREFIX;
  private int cores = Constants.DEFAULT_CORE_THREADS;
  private int threads = Constants.DEFAULT_THREADS;
  private int queues = Constants.DEFAULT_QUEUES;
  private int alive = Constants.DEFAULT_ALIVE;
  private boolean daemon = Constants.DEFAULT_THREAD_DAEMON;
}
