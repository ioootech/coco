package tech.iooo.boot;

import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Hello world!
 *
 * @author Ivan97
 */
public class App {
	private static final Logger logger = LoggerFactory.getLogger(App.class);

	public static void main(String[] args) {
		logger.info("hello world");
		logger.debug("DEBUG");
		logger.trace("TRACE");
		Vertx.vertx().setPeriodic(1000, handler -> {
			logger.info("{}", System.currentTimeMillis());
		});
	}
}
