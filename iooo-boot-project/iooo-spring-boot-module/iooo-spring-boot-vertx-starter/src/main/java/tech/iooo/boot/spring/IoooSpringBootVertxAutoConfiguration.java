package tech.iooo.boot.spring;

import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.file.FileSystem;
import io.vertx.core.shareddata.SharedData;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Ivan97
 */
@Configuration
public class IoooSpringBootVertxAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public Vertx vertx() {
		return Vertx.vertx();
	}

	@Bean
	@ConditionalOnMissingBean
	public EventBus eventBus(Vertx vertx) {
		return vertx.eventBus();
	}

	@Bean
	@ConditionalOnMissingBean
	public FileSystem fileSystem(Vertx vertx) {
		return vertx.fileSystem();
	}

	@Bean
	@ConditionalOnMissingBean
	public SharedData sharedData(Vertx vertx) {
		return vertx.sharedData();
	}
}
