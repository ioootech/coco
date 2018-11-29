package tech.iooo.boot.core.spring.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import tech.iooo.boot.core.utils.Version;

/**
 * Created on 2018-11-26 09:37
 *
 * @author <a href="mailto:yangkizhang@gmail.com?subject=iooo-boot">Ivan97</a>
 */
@Component
public class IoooBootVersionInfoApplicationListener implements ApplicationListener<ApplicationStartedEvent>, Ordered {

  private static final Logger logger = LoggerFactory.getLogger(IoooBootVersionInfoApplicationListener.class);

  @Override
  public void onApplicationEvent(ApplicationStartedEvent event) {
    if (logger.isDebugEnabled()) {
      logger.debug("Running with Iooo Boot [v{}]", Version.getVersion());
    }
  }

  @Override
  public int getOrder() {
    return Ordered.HIGHEST_PRECEDENCE;
  }
}
