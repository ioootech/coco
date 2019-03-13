package tech.iooo.boot.spring.configuration;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.SmartLifecycle;
import org.springframework.context.annotation.Configuration;
import tech.iooo.boot.core.utils.ClassUtils;
import tech.iooo.boot.spring.annotation.VerticleService;

/**
 * Created on 2018/8/24 上午11:01
 *
 * @author <a href="mailto:yangkizhang@gmail.com?subject=iooo-spring-boot-vertx-bundle">Ivan97</a>
 */
@Configuration
public class IoooVertxApplicationBooster implements SmartLifecycle, ApplicationContextAware {

  private static final Logger logger = LoggerFactory.getLogger(IoooVertxApplicationBooster.class);
  @Autowired
  private Vertx vertx;

  @Autowired
  private IoooVertxProperties ioooVertxProperties;

  private boolean running;

  private ApplicationContext applicationContext;

  @Override
  public boolean isAutoStartup() {
    return true;
  }

  @Override
  public void stop(Runnable callback) {
    vertx.close();
    callback.run();
    this.running = false;
  }

  @Override
  public void start() {
    applicationContext.getBeansWithAnnotation(VerticleService.class)
        .forEach((name, bean) -> {
          AbstractVerticle verticle = (AbstractVerticle) bean;
          IoooVerticleServicesHolder.activeVerticleServices().put(bean.getClass().getName(), "", verticle);
        });
    IoooVerticleServicesHolder.activeVerticleServices().values().forEach(verticle -> {
      Class verticleClass = verticle.getClass();
      VerticleService verticleService = verticle.getClass().getAnnotation(VerticleService.class);
      DeploymentOptions deploymentOptions;
      String optionName;
      if (ioooVertxProperties.getVerticle().isFailFast()) {
        optionName = verticleService.deploymentOption();
        deploymentOptions = applicationContext.getBean(optionName, DeploymentOptions.class);
      } else {
        if (applicationContext.containsBean(verticleService.deploymentOption())) {
          optionName = verticleService.deploymentOption();
          deploymentOptions = applicationContext.getBean(optionName, DeploymentOptions.class);
        } else {
          logger.warn("failed to get deploymentOption [{}] during the deployment of verticle [{}],use default deployment options instead.",
              verticleService.deploymentOption(), verticleClass.getSimpleName());
          optionName = VertxConfigConstants.DEFAULT_DEPLOYMENT_OPTIONS;
          deploymentOptions = applicationContext.getBean(optionName, DeploymentOptions.class);
        }
      }
      vertx.deployVerticle(VertxConfigConstants.IOOO_VERTICLE_PREFIX + ":" + verticleClass.getName(),
          deploymentOptions,
          res -> {
            if (res.succeeded()) {
              if (logger.isInfoEnabled()) {
                String className = ClassUtils.isCglibProxyClass(verticleClass) ? ClassUtils.getUserClass(verticleClass).getSimpleName() : verticleClass.getSimpleName();
                logger.info("deployed verticle [{}] with deploymentOption [{}],id [{}].", className, optionName, res.result());
              }
              IoooVerticleServicesHolder.activeVerticleServices().row(verticleClass.getName()).remove("");
              IoooVerticleServicesHolder.activeVerticleServices().row(verticleClass.getName()).put(res.result(), verticle);
            } else {
              logger.error("error with deploy verticle " + verticleClass.getName(), res.cause());
            }
          });
    });
    this.running = true;
  }

  @Override
  public void stop() {
    stop(() -> IoooVerticleServicesHolder.activeVerticleServices().columnKeySet().forEach(verticle -> vertx.undeploy(verticle, res -> {
      if (res.succeeded()) {
        if (logger.isInfoEnabled()) {
          logger.info("unload verticle {} ", verticle);
        }
      } else {
        logger.error("something happened while unload verticle " + verticle, res.cause());
      }
    })));
  }

  @Override
  public boolean isRunning() {
    return this.running;
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
  }
}
