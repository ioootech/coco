package tech.iooo.boot.spring.configuration;

import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

/**
 * Created on 2019-04-14 14:06
 *
 * @author <a href="mailto:yangkizhang@gmail.com?subject=iooo-boot">Ivan97</a>
 */
@Component
public class IoooVertxPostProcessor implements BeanPostProcessor, ApplicationContextAware {

  private static final Logger logger = LoggerFactory.getLogger(IoooVertxPostProcessor.class);
  @Autowired
  private ApplicationContext applicationContext;

  @Override
  public Object postProcessAfterInitialization(@NonNull Object bean, String beanName) throws BeansException {
    if (bean instanceof Vertx) {
      IoooVerticleFactory factory = applicationContext.getBean(IoooVerticleFactory.class);
      if (!((Vertx) bean).verticleFactories().contains(factory)) {
        ((Vertx) bean).registerVerticleFactory(factory);
        if (logger.isDebugEnabled()) {
          logger.debug("VerticleFactory register");
        }
      }
    }
    return bean;
  }

  @Override
  public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
  }
}
