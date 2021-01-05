package tech.iooo.boot.spring.configuration;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.context.annotation.DeferredImportSelector;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.lang.NonNull;

/**
 * @author 龙也
 * @date 2020/5/19 10:05 上午
 */
public class VertxGatewaySelector implements DeferredImportSelector, BeanFactoryAware {

  private BeanFactory beanFactory;

  @Override
  public String[] selectImports(@NonNull AnnotationMetadata annotationMetadata) {
    return ArrayUtils.toArray(IoooGatewayConfiguration.class.getName());
  }

  @Override
  public void setBeanFactory(@NonNull BeanFactory beanFactory) throws BeansException {
    this.beanFactory = beanFactory;
  }
}
