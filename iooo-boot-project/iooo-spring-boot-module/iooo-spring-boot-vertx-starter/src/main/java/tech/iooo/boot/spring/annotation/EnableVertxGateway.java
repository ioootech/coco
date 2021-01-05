package tech.iooo.boot.spring.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.context.annotation.Import;
import tech.iooo.boot.spring.configuration.VertxGatewaySelector;

/**
 * @author 龙也
 * @date 2020/5/19 10:08 上午
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target(ElementType.TYPE)
@Import(VertxGatewaySelector.class)
public @interface EnableVertxGateway {

}
