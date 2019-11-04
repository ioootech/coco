package tech.iooo.boot.spring.configuration;

import static tech.iooo.boot.spring.configuration.VertxConfigConstants.DEFAULT_DEPLOYMENT_OPTIONS;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.file.FileSystem;
import io.vertx.core.json.JsonObject;
import io.vertx.core.shareddata.SharedData;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tech.iooo.boot.core.spring.component.SpringUtils;

/**
 * Created on 2018/8/24 上午10:56
 *
 * @author <a href="mailto:yangkizhang@gmail.com?subject=iooo-spring-boot-vertx-bundle">Ivan97</a>
 */
@Configuration
@EnableConfigurationProperties(IoooVertxProperties.class)
public class IoooVertxConfiguration {

  @Autowired
  private IoooVertxProperties ioooVertxProperties;

  @Bean
  @ConditionalOnMissingBean
  public Vertx vertx() {
    IoooVerticleFactory factory = SpringUtils.getBean(IoooVerticleFactory.class);
    Vertx vertx = Vertx.vertx();
    vertx.registerVerticleFactory(factory);
    return vertx;
  }

  @Bean
  @ConditionalOnMissingBean
  public EventBus eventBus(Vertx vertx) {
    return vertx.eventBus();
  }

  @Bean
  @ConditionalOnMissingBean
  public FileSystem fileSystem(Vertx vertx) {
    return vertx.fileSystem();
  }

  @Bean
  @ConditionalOnMissingBean
  public SharedData sharedData(Vertx vertx) {
    return vertx.sharedData();
  }

  @Bean(DEFAULT_DEPLOYMENT_OPTIONS)
  public DeploymentOptions deploymentOptions() {
    DeploymentOptions deploymentOptions = new DeploymentOptions();
    Optional.ofNullable(ioooVertxProperties.getDefaultDeploymentOption().getConfig()).ifPresent(item -> deploymentOptions.setConfig(JsonObject.mapFrom(item)));
    Optional.ofNullable(ioooVertxProperties.getDefaultDeploymentOption().getExtraClasspath()).ifPresent(deploymentOptions::setExtraClasspath);
    Optional.ofNullable(ioooVertxProperties.getDefaultDeploymentOption().getHa()).ifPresent(deploymentOptions::setHa);
    Optional.ofNullable(ioooVertxProperties.getDefaultDeploymentOption().getInstances()).ifPresent(deploymentOptions::setInstances);
    Optional.ofNullable(ioooVertxProperties.getDefaultDeploymentOption().getIsolatedClasses()).ifPresent(deploymentOptions::setIsolatedClasses);
    Optional.ofNullable(ioooVertxProperties.getDefaultDeploymentOption().getIsolationGroup()).ifPresent(deploymentOptions::setIsolationGroup);
    Optional.ofNullable(ioooVertxProperties.getDefaultDeploymentOption().getMaxWorkerExecuteTime()).ifPresent(deploymentOptions::setMaxWorkerExecuteTime);
    Optional.ofNullable(ioooVertxProperties.getDefaultDeploymentOption().getMaxWorkerExecuteTimeUnit()).ifPresent(deploymentOptions::setMaxWorkerExecuteTimeUnit);
    Optional.ofNullable(ioooVertxProperties.getDefaultDeploymentOption().getMultiThreaded()).ifPresent(deploymentOptions::setMultiThreaded);
    Optional.ofNullable(ioooVertxProperties.getDefaultDeploymentOption().getWorker()).ifPresent(deploymentOptions::setWorker);
    Optional.ofNullable(ioooVertxProperties.getDefaultDeploymentOption().getWorkerPoolName()).ifPresent(deploymentOptions::setWorkerPoolName);
    Optional.ofNullable(ioooVertxProperties.getDefaultDeploymentOption().getWorkerPoolSize()).ifPresent(deploymentOptions::setWorkerPoolSize);
    return deploymentOptions;
  }
}
