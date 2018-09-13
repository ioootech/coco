package tech.iooo.boot.spring.endpoint;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.vertx.core.Vertx;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.annotation.DeleteOperation;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.Selector;
import org.springframework.boot.actuate.health.Health;
import tech.iooo.boot.spring.configuration.IoooVerticleServicesHolder;

/**
 * Created on 2018/9/12 下午2:58
 *
 * @author <a href="mailto:yangkizhang@gmail.com?subject=iooo-boot">Ivan97</a>
 */
@Endpoint(id = "verticles/undeploy")
public class IoooUndeployVerticleEndpoint {

	private static final Logger logger = LoggerFactory.getLogger(IoooUndeployVerticleEndpoint.class);
	@Autowired
	private Vertx vertx;

	@DeleteOperation
	public Health undeploy(@Selector String id) {
		if (vertx.deploymentIDs().contains(id)) {
			vertx.undeploy(id);
			String name = IoooVerticleServicesHolder.activeVerticleServices().columnMap().get(id).keySet().iterator().next();
			logger.info("undeploy verticle [{}],id [{}].", name, id);

			List<Object> active = Lists.newArrayList();
			List<Object> inactive = Lists.newArrayList();

			synchronized (IoooVerticleServicesHolder.class) {
				IoooVerticleServicesHolder.inactiveVerticleServices().put(name, id, IoooVerticleServicesHolder.activeVerticleServices().get(name, id));
				IoooVerticleServicesHolder.activeVerticleServices().columnKeySet().remove(id);

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
			}

			return Health.up()
					.withDetail("verticles", active)
					.withDetail("inactive verticles", inactive)
					.withDetail("timestamp", LocalDateTime.now())
					.build();
		} else {
			return Health.unknown().withDetail("message", "unknown verticle").withDetail("timestamp", LocalDateTime.now()).build();
		}
	}
}
