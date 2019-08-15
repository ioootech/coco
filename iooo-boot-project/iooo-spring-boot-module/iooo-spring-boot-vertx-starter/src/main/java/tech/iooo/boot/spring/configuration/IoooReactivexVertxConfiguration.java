package tech.iooo.boot.spring.configuration;

import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.core.eventbus.EventBus;
import io.vertx.reactivex.core.file.FileSystem;
import io.vertx.reactivex.core.shareddata.SharedData;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * Created on 2018/8/24 上午10:56
 *
 * @author <a href="mailto:yangkizhang@gmail.com?subject=iooo-spring-boot-vertx-bundle">Ivan97</a>
 */
@ConditionalOnClass(Vertx.class)
@EnableConfigurationProperties(IoooVertxProperties.class)
public class IoooReactivexVertxConfiguration {

  @Bean
  public Vertx reactivexVertx(io.vertx.core.Vertx vertx) {
    return new Vertx(vertx);
  }

  @Bean
  @ConditionalOnBean(Vertx.class)
  public EventBus reactivexEventBus(Vertx vertx) {
    return vertx.eventBus();
  }

  @Bean
  @ConditionalOnBean(Vertx.class)
  public FileSystem reactivexFileSystem(Vertx vertx) {
    return vertx.fileSystem();
  }

  @Bean
  @ConditionalOnBean(Vertx.class)
  public SharedData reactivexSharedData(Vertx vertx) {
    return vertx.sharedData();
  }
}
