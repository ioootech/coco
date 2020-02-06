package tech.iooo.boot.spring.configuration;

import java.util.List;
import java.util.concurrent.TimeUnit;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created on 2018/9/2 下午4:21
 *
 * @author <a href="mailto:yangkizhang@gmail.com?subject=iooo-boot">Ivan97</a>
 */
@Data
@ConfigurationProperties(prefix = "vertx", ignoreInvalidFields = true)
public class IoooVertxProperties {

  private IoooVertxProperties.Verticle verticle = new IoooVertxProperties.Verticle();
  private DefaultDeploymentOption defaultDeploymentOption = new IoooVertxProperties.DefaultDeploymentOption();
  private Gateway gateway = new IoooVertxProperties.Gateway();

  @Data
  public static class Verticle {

    /**
     * deploy过程是否开启快速失败
     */
    private boolean failFast = false;
  }

  @Data
  public static class DefaultDeploymentOption {

    private String config;
    private List<String> extraClasspath;
    private Boolean ha;
    private Integer instances;
    private List<String> isolatedClasses;
    private String isolationGroup;
    private Long maxWorkerExecuteTime;
    private TimeUnit maxWorkerExecuteTimeUnit;
    @Deprecated
    private Boolean multiThreaded;
    private Boolean worker;
    private String workerPoolName;
    private Integer workerPoolSize;
  }

  @Data
  public static class Gateway {

    private boolean enable = false;
    private Integer port = 9090;
  }
}
