package tech.iooo.boot.spring.annotation;

import io.vertx.core.http.HttpMethod;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author 龙也
 * @date 2019-08-15 20:45
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface RequestMapping {

  String path();

  HttpMethod method() default HttpMethod.GET;
}
