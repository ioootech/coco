package tech.iooo.boot.spring.configuration;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.SmartLifecycle;
import org.springframework.context.annotation.Configuration;
import tech.iooo.boot.spring.annotation.VerticleService;

/**
 * Created on 2018/8/24 上午11:01
 *
 * @author <a href="mailto:yangkizhang@gmail.com?subject=iooo-spring-boot-vertx-bundle">Ivan97</a>
 */
@Configuration
public class IoooVertxApplicationBooster implements SmartLifecycle, ApplicationContextAware {

	private static final Logger logger = LoggerFactory.getLogger(IoooVertxApplicationBooster.class);
	@Autowired
	private Vertx vertx;

	@Autowired
	private IoooVertxProperties ioooVertxProperties;

	private boolean running;

	private ApplicationContext applicationContext;

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
		applicationContext.getBeansWithAnnotation(VerticleService.class)
				.forEach((name, bean) -> {
					AbstractVerticle verticle = (AbstractVerticle) bean;
					IoooVerticleServicesHolder.verticleServices().put(bean.getClass().getName(), "", verticle);
				});
		IoooVerticleServicesHolder.verticleServices().values().forEach(verticle -> {
			Class verticleClass = verticle.getClass();
			VerticleService verticleService = verticle.getClass().getAnnotation(VerticleService.class);
			DeploymentOptions deploymentOptions;
			String optionName;
			if (ioooVertxProperties.getVerticle().isFailFast()) {
				deploymentOptions = applicationContext.getBean(verticleService.deploymentOption(), DeploymentOptions.class);
				optionName = verticleService.deploymentOption();
			} else {
				if (applicationContext.containsBean(verticleService.deploymentOption())) {
					deploymentOptions = applicationContext.getBean(verticleService.deploymentOption(), DeploymentOptions.class);
					optionName = verticleService.deploymentOption();
				} else {
					logger.warn("failed to get deploymentOption [{}] during the deployment of verticle [{}],use default deployment options instead.",
							verticleService.deploymentOption(), verticleClass.getSimpleName());
					deploymentOptions = applicationContext.getBean(VertxConfigConstants.DEFAULT_DEPLOYMENT_OPTIONS, DeploymentOptions.class);
					optionName = VertxConfigConstants.DEFAULT_DEPLOYMENT_OPTIONS;
				}
			}
			vertx.deployVerticle(VertxConfigConstants.IOOO_VERTICLE_PREFIX + ":" + verticleClass.getName(),
					deploymentOptions,
					res -> {
						if (res.succeeded()) {
							logger.info("deployed verticle [{}] with deploymentOption [{}],id [{}].", verticleClass.getSimpleName(), optionName, res.result());
							IoooVerticleServicesHolder.verticleServices().row(verticleClass.getName()).remove("");
							IoooVerticleServicesHolder.verticleServices().row(verticleClass.getName()).put(res.result(), verticle);
						} else {
							logger.error("error with deploy verticle " + verticleClass.getName(), res.cause());
						}
					});
		});
		this.running = true;
	}

	@Override
	public void stop() {
		stop(() -> IoooVerticleServicesHolder.verticleServices().columnKeySet().forEach(verticle -> vertx.undeploy(verticle)));
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
