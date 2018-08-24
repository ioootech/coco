package tech.iooo.boot.spring.annotation;

import static tech.iooo.boot.spring.configuration.VertxConfigConstants.DEFAULT_DEPLOYMENT_OPTIONS;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 * Created on 2018/8/24 上午11:04
 *
 * @author <a href="mailto:yangkizhang@gmail.com?subject=iooo-spring-boot-vertx-bundle">Ivan97</a>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public @interface VerticleService {

	String deploymentOption() default DEFAULT_DEPLOYMENT_OPTIONS;
}
