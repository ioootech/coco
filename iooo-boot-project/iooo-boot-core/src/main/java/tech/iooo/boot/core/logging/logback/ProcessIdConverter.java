package tech.iooo.boot.core.logging.logback;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import java.lang.management.ManagementFactory;

/**
 * Created on 2018/9/8 下午9:04
 *
 * @author <a href="mailto:yangkizhang@gmail.com?subject=iooo-boot">Ivan97</a>
 */
public class ProcessIdConverter extends ClassicConverter {

  private static final String PROCESS_ID = ManagementFactory.getRuntimeMXBean().getName().replaceAll("@.*", "");

  @Override
  public String convert(final ILoggingEvent event) {
    // for every logging event return processId from mx bean
    // (or better alternative)
    return PROCESS_ID;
  }
}
