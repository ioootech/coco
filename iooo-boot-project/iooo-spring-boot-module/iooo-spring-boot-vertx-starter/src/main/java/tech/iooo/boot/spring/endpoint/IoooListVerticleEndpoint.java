package tech.iooo.boot.spring.endpoint;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.vertx.core.Vertx;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.Status;
import tech.iooo.boot.spring.configuration.IoooVerticleServicesHolder;

/**
 * Created on 2018/9/12 下午2:58
 *
 * @author <a href="mailto:yangkizhang@gmail.com?subject=iooo-boot">Ivan97</a>
 */
@Endpoint(id = "verticles")
public class IoooListVerticleEndpoint {

	@Autowired
	private Vertx vertx;

	@ReadOperation
	public Health getCustom() {
		if (Objects.isNull(vertx)) {
			return Health.down().withDetail("message", "vertx inactive").status(Status.DOWN).build();
		}
		List<Object> list = Lists.newArrayList();
		IoooVerticleServicesHolder.verticleServices.cellSet().forEach(cell -> {
			Map<String, Object> detail = Maps.newHashMap();
			detail.put("name", cell.getRowKey());
			detail.put("id", cell.getColumnKey());
			list.add(detail);
		});
		return Health.up().withDetail("verticles", list).withDetail("timestamp", LocalDateTime.now()).status(Status.UP).build();
	}
}
