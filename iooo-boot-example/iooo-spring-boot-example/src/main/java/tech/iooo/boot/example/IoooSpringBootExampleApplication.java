package tech.iooo.boot.example;

import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Ivan97
 */
@SpringBootApplication
public class IoooSpringBootExampleApplication implements CommandLineRunner {

	private static final Logger logger = LoggerFactory.getLogger(IoooSpringBootExampleApplication.class);
	@Autowired
	private Vertx vertx;

	public static void main(String[] args) {
		SpringApplication.run(IoooSpringBootExampleApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		vertx.setPeriodic(1000, id -> logger.info("id:[{}]", id));
	}
}
