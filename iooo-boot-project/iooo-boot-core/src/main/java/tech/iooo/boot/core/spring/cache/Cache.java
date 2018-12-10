package tech.iooo.boot.core.spring.cache;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.time.temporal.ChronoUnit;

/**
 * Created on 2018-12-10 14:02
 *
 * @author <a href="mailto:yangkizhang@gmail.com?subject=iooo-boot">Ivan97</a>
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Cache {

  //时间刻度，天、时、分...
  ChronoUnit timeScale() default ChronoUnit.SECONDS;

  //时间间隔
  int timeInterval() default 0;
}
