package tech.iooo.boot.core.spring.component;

import java.lang.annotation.Annotation;
import java.util.Map;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.env.Environment;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

/**
 * @author yanchi.zyc
 * @date 2019-06-03
 */
@Component
public class SpringUtils implements ApplicationContextAware {

  private static ApplicationContext APPLICATION_CONTEXT;

  @Override
  public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
    APPLICATION_CONTEXT = applicationContext;
  }

  public static <T> T getBean(Class<T> clazz) {
    return APPLICATION_CONTEXT.getBean(clazz);
  }

  public static Object getBean(String clazz) {
    return APPLICATION_CONTEXT.getBean(clazz);
  }

  public static <T> Map<String, T> getBeansOfType(Class<T> tClass) {
    return APPLICATION_CONTEXT.getBeansOfType(tClass);
  }

  public static Map<String, Object> getBeansWithAnnotation(Class<? extends Annotation> tClass) {
    return APPLICATION_CONTEXT.getBeansWithAnnotation(tClass);
  }

  public static Environment getEnvironment() {
    return APPLICATION_CONTEXT.getEnvironment();
  }
}
