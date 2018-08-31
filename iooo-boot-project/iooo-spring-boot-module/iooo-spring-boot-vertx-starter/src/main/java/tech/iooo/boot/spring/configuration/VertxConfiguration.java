package tech.iooo.boot.spring.configuration;

import static tech.iooo.boot.spring.configuration.VertxConfigConstants.DEFAULT_DEPLOYMENT_OPTIONS;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.file.FileSystem;
import io.vertx.core.shareddata.SharedData;
import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created on 2018/8/24 上午10:56
 *
 * @author <a href="mailto:yangkizhang@gmail.com?subject=iooo-spring-boot-vertx-bundle">Ivan97</a>
 */
@Configuration
public class VertxConfiguration implements ApplicationContextAware {

	private ApplicationContext applicationContext;

	@Bean
	@ConditionalOnMissingBean
	public VertxOptions vertxOptions() {
		return new VertxOptions();
	}

	@Bean
	@ConditionalOnMissingBean
	public Vertx vertx(VertxOptions vertxOptions) {
		VertxApplicationBooster booster = applicationContext.getBean(VertxApplicationBooster.class);
		Vertx vertx = Vertx.vertx(vertxOptions);
		vertx.registerVerticleFactory(booster);
		return vertx;
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

	@Bean(DEFAULT_DEPLOYMENT_OPTIONS)
	public DeploymentOptions deploymentOptions() {
		return new DeploymentOptions();
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
}
