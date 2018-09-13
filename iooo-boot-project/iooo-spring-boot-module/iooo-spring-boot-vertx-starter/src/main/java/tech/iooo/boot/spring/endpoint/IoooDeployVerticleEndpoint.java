package tech.iooo.boot.spring.endpoint;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.Selector;
import org.springframework.boot.actuate.endpoint.annotation.WriteOperation;
import org.springframework.boot.actuate.health.Health;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import tech.iooo.boot.spring.configuration.IoooVerticleServicesHolder;
import tech.iooo.boot.spring.configuration.VertxConfigConstants;

/**
 * Created on 2018/9/12 下午2:58
 *
 * @author <a href="mailto:yangkizhang@gmail.com?subject=iooo-boot">Ivan97</a>
 */
@Endpoint(id = "verticles/deploy")
public class IoooDeployVerticleEndpoint implements ApplicationContextAware {

	private static final Logger logger = LoggerFactory.getLogger(IoooDeployVerticleEndpoint.class);
	@Autowired
	private Vertx vertx;

	private ApplicationContext applicationContext;

	@WriteOperation
	public Health deploy(@Selector String info) {
		List<String> infos = Splitter.on("#").limit(1).splitToList(info);
		String clazz = infos.get(0);
		String deploymentConfig;
		if (info.length() == 1) {
			deploymentConfig = infos.get(1);
		} else {
			deploymentConfig = VertxConfigConstants.DEFAULT_DEPLOYMENT_OPTIONS;
		}
		if (!applicationContext.containsBean(deploymentConfig)) {
			return Health.unknown()
					.withDetail("message", "unknown deploymentOptions " + deploymentConfig)
					.withDetail("timestamp", LocalDateTime.now())
					.build();
		}

		if (IoooVerticleServicesHolder.activeVerticleServices().rowKeySet().contains(clazz)) {
			AbstractVerticle verticle = IoooVerticleServicesHolder.activeVerticleServices().rowMap().get(clazz).values().iterator().next();
			return Health.up()
					.withDetail("message", "already deployed")
					.withDetail("config", verticle.config())
					.withDetail("timestamp", LocalDateTime.now())
					.build();
		}

		if (IoooVerticleServicesHolder.inactiveVerticleServices().rowKeySet().contains(clazz)) {
			DeploymentOptions deploymentOptions = (DeploymentOptions) applicationContext.getBean(deploymentConfig);

			CompletableFuture<Health> healthFuture = new CompletableFuture<>();
			vertx.deployVerticle(clazz, deploymentOptions, res -> {
				if (res.succeeded()) {
					synchronized (IoooVerticleServicesHolder.class) {
						IoooVerticleServicesHolder.inactiveVerticleServices().rowKeySet().remove(clazz);
						try {
							IoooVerticleServicesHolder.activeVerticleServices().put(clazz, res.result(), ((AbstractVerticle) BeanUtils.instantiateClass(Class.forName(clazz))));
						} catch (ClassNotFoundException e) {
							e.printStackTrace();
						}
						logger.info("deployed verticle [{}] with deploymentOption [{}],id [{}].", clazz, deploymentConfig, res.result());

						List<Object> active = Lists.newArrayList();
						List<Object> inactive = Lists.newArrayList();

						IoooVerticleServicesHolder.activeVerticleServices().cellSet().forEach(cell -> {
							Map<String, Object> detail = Maps.newHashMap();
							detail.put("name", cell.getRowKey());
							detail.put("id", cell.getColumnKey());
							active.add(detail);
						});
						IoooVerticleServicesHolder.inactiveVerticleServices().cellSet().forEach(cell -> {
							Map<String, Object> detail = Maps.newHashMap();
							detail.put("name", cell.getRowKey());
							detail.put("id", cell.getColumnKey());
							inactive.add(detail);
						});
						healthFuture.complete(Health.up()
								.withDetail("verticles", active)
								.withDetail("inactive verticles", inactive)
								.withDetail("timestamp", LocalDateTime.now())
								.build());
					}
				} else {
					healthFuture.complete(Health.down()
							.withException(res.cause())
							.withDetail("timestamp", LocalDateTime.now())
							.build());
					logger.error("", res.cause());
				}
			});
			try {
				return healthFuture.get();
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		}

		return Health.up().build();
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
}
