package tech.iooo.boot.core.logging.logback;

import static ch.qos.logback.core.pattern.color.ANSIConstants.BLUE_FG;
import static ch.qos.logback.core.pattern.color.ANSIConstants.BOLD;
import static ch.qos.logback.core.pattern.color.ANSIConstants.CYAN_FG;
import static ch.qos.logback.core.pattern.color.ANSIConstants.DEFAULT_FG;
import static ch.qos.logback.core.pattern.color.ANSIConstants.GREEN_FG;
import static ch.qos.logback.core.pattern.color.ANSIConstants.RED_FG;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.pattern.color.ForegroundCompositeConverterBase;

/**
 * Created on 2018/9/8 下午7:58
 *
 * @author <a href="mailto:yangkizhang@gmail.com?subject=iooo-boot">Ivan97</a>
 */
public class IoooHighlightingCompositeConverter extends ForegroundCompositeConverterBase<ILoggingEvent> {

  @Override
  protected String getForegroundColorCode(ILoggingEvent event) {
    Level level = event.getLevel();
    switch (level.toInt()) {
      case Level.DEBUG_INT:
        return CYAN_FG;
      case Level.ERROR_INT:
        return BOLD + RED_FG;
      case Level.WARN_INT:
        return RED_FG;
      case Level.INFO_INT:
        return GREEN_FG;
      case Level.TRACE_INT:
        return BLUE_FG;
      default:
        return DEFAULT_FG;
    }
  }
}
