package tech.iooo.boot.spring.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * @author 龙也
 * @date 2019/12/3 7:46 下午
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface VerticleDeploymentOption {

  String[] extraClasspath() default "";

  boolean ha() default false;

  int instances() default 1;

  String isolationGroup() default "";

  long maxWorkerExecuteTime() default 60000000000L;

  TimeUnit maxWorkerExecuteTimeUnit() default TimeUnit.NANOSECONDS;

  boolean worker() default false;

  String workerPoolName() default "";

  int workerPoolSize() default 20;
}
