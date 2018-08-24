package tech.iooo.boot.spring.configuration;

import com.google.common.collect.Lists;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.SmartLifecycle;
import org.springframework.stereotype.Component;
import tech.iooo.boot.spring.annotation.VerticleService;

/**
 * Created on 2018/8/24 上午11:01
 *
 * @author <a href="mailto:yangkizhang@gmail.com?subject=iooo-spring-boot-vertx-bundle">Ivan97</a>
 */
@Component
public class VertxApplicationLifecycle implements SmartLifecycle, ApplicationContextAware {

	private static final Logger logger = LoggerFactory.getLogger(VertxApplicationLifecycle.class);
	@Autowired
	private Vertx vertx;

	private boolean running;

	private ApplicationContext applicationContext;

	private List<String> deployedVerticles = Lists.newArrayList();

	@Override
	public boolean isAutoStartup() {
		return true;
	}

	@Override
	public void stop(Runnable callback) {
		callback.run();
		this.running = false;
	}

	@Override
	public void start() {
		Map<String, Object> verticleServices = applicationContext.getBeansWithAnnotation(VerticleService.class);
		verticleServices.values().forEach(verticle -> {
			Class verticleClass = verticle.getClass();
			VerticleService verticleService = verticle.getClass().getAnnotation(VerticleService.class);
			DeploymentOptions deploymentOptions = applicationContext
					.getBean(verticleService.deploymentOption(), DeploymentOptions.class);
			vertx.deployVerticle(VertxConfigConstants.IOOO_VERTICLE_PREFIX + ":" + verticleClass.getName(), deploymentOptions,
					res -> {
						if (res.succeeded()) {
							logger.info("deployed verticle [{}] with deploymentOption [{}]. id [{}]",
									verticleClass.getSimpleName(), verticleService.deploymentOption(), res.result());
							deployedVerticles.add(res.result());
						} else {
							logger.error("error with deploy verticle " + verticleClass.getName(), res.cause());
						}
					});
		});
		this.running = true;
	}

	@Override
	public void stop() {
		this.running = false;
	}

	@Override
	public boolean isRunning() {
		return this.running;
	}

	@Override
	public int getPhase() {
		return Integer.MAX_VALUE;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
}
