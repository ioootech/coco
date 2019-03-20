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

  public static final ThreadPoolConfig DEFAULT_CONFIG = new ThreadPoolConfig.ThreadPoolConfigBuilder().build();

  private String name = Constants.DEFAULT_THREAD_NAME;
  private int cores = Constants.DEFAULT_CORE_THREADS;
  private int threads = Integer.MAX_VALUE;
  private int queues = Constants.DEFAULT_QUEUES;
  private int alive = Constants.DEFAULT_ALIVE;
}
