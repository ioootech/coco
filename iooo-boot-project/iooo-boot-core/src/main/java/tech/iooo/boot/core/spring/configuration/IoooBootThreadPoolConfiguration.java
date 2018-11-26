package tech.iooo.boot.core.spring.configuration;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tech.iooo.boot.core.threadpool.support.AbortPolicyWithReport;

/**
 * Created on 2018-11-26 09:35
 *
 * @author <a href="mailto:yangkizhang@gmail.com?subject=iooo-boot">Ivan97</a>
 */
@Configuration
public class IoooBootThreadPoolConfiguration {

  @Bean
  public ExecutorService executorService() {
    ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("i-exec-pool-%d").setDaemon(true).build();
    return new ThreadPoolExecutor(5, 200, 0L, TimeUnit.MILLISECONDS,
        new LinkedBlockingDeque<>(1024), threadFactory, new AbortPolicyWithReport());
  }

  @Bean
  public ScheduledExecutorService scheduledExecutorService() {
    return new ScheduledThreadPoolExecutor(1, new BasicThreadFactory.Builder().namingPattern("i-scheduled-pool-%d").daemon(true).build());
  }
}
