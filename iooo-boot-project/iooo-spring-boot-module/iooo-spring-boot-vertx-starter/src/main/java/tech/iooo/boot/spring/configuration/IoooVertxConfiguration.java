package tech.iooo.boot.spring.configuration;

import static tech.iooo.boot.spring.configuration.VertxConfigConstants.DEFAULT_DEPLOYMENT_OPTIONS;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.file.FileSystem;
import io.vertx.core.shareddata.SharedData;
import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;

/**
 * Created on 2018/8/24 上午10:56
 *
 * @author <a href="mailto:yangkizhang@gmail.com?subject=iooo-spring-boot-vertx-bundle">Ivan97</a>
 */
@Configuration
@EnableConfigurationProperties(IoooVertxProperties.class)
public class IoooVertxConfiguration implements ApplicationContextAware {

  private ApplicationContext applicationContext;

  @Bean
  @ConditionalOnMissingBean
  public Vertx vertx() {
    IoooVerticleFactory factory = applicationContext.getBean(IoooVerticleFactory.class);
    Vertx vertx = Vertx.vertx();
    vertx.registerVerticleFactory(factory);
    return vertx;
  }

  @Bean
  @ConditionalOnBean(Vertx.class)
  @ConditionalOnClass(io.vertx.reactivex.core.Vertx.class)
  public io.vertx.reactivex.core.Vertx reactivexVertx(Vertx vertx) {
    return new io.vertx.reactivex.core.Vertx(vertx);
  }

  @Bean
  @ConditionalOnMissingBean
  public EventBus eventBus(Vertx vertx) {
    return vertx.eventBus();
  }

  @Bean
  @ConditionalOnBean(io.vertx.reactivex.core.Vertx.class)
  public io.vertx.reactivex.core.eventbus.EventBus reactivexEventBus(io.vertx.reactivex.core.Vertx vertx) {
    return vertx.eventBus();
  }

  @Bean
  @ConditionalOnMissingBean
  public FileSystem fileSystem(Vertx vertx) {
    return vertx.fileSystem();
  }

  @Bean
  @ConditionalOnBean(io.vertx.reactivex.core.Vertx.class)
  public io.vertx.reactivex.core.file.FileSystem reactivexFileSystem(io.vertx.reactivex.core.Vertx vertx) {
    return vertx.fileSystem();
  }

  @Bean
  @ConditionalOnMissingBean
  public SharedData sharedData(Vertx vertx) {
    return vertx.sharedData();
  }

  @Bean
  @ConditionalOnBean(io.vertx.reactivex.core.Vertx.class)
  public io.vertx.reactivex.core.shareddata.SharedData reactivexSharedData(io.vertx.reactivex.core.Vertx vertx) {
    return vertx.sharedData();
  }

  @Bean(DEFAULT_DEPLOYMENT_OPTIONS)
  public DeploymentOptions deploymentOptions() {
    return new DeploymentOptions();
  }

  @Override
  public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
  }
}
