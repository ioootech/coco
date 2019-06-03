package tech.iooo.boot.core.spring.component;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import tech.iooo.boot.core.constants.SuppressTypeConstants;

/**
 * @author yanchi.zyc
 * @date 2019-06-03
 */
@Component
public class SpringUtils implements ApplicationContextAware {

  private static ApplicationContext APPLICATION_CONTEXT = null;

  @Override
  public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
    APPLICATION_CONTEXT = applicationContext;
  }

  @SuppressWarnings(SuppressTypeConstants.UNCHECKED)
  public static <T> T getBean(String name) {
    return (T) APPLICATION_CONTEXT.getBean(name);
  }

  public static <T> T getBean(Class<T> clazz) {
    return APPLICATION_CONTEXT.getBean(clazz);
  }
}
