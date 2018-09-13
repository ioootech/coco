package tech.iooo.boot.spring.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created on 2018/9/2 下午4:21
 *
 * @author <a href="mailto:yangkizhang@gmail.com?subject=iooo-boot">Ivan97</a>
 */
@Data
@ConfigurationProperties(prefix = "vertx")
public class IoooVertxProperties {

  private IoooVertxProperties.Verticle verticle = new IoooVertxProperties.Verticle();

  @Data
  public static class Verticle {

    /**
     * deploy过程是否开启快速失败
     */
    private boolean failFast = false;
  }
}
