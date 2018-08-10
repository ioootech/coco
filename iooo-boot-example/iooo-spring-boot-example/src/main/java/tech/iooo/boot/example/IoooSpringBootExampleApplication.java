package tech.iooo.boot.example;

import io.vertx.core.Vertx;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Ivan97
 */
@SpringBootApplication
public class IoooSpringBootExampleApplication implements CommandLineRunner {
	
	@Autowired
	private Vertx vertx;
	
	public static void main(String[] args) {
		SpringApplication.run(IoooSpringBootExampleApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		
	}
}
