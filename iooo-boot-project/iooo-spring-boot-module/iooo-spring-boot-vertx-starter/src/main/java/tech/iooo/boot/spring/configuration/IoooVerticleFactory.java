package tech.iooo.boot.spring.configuration;

import io.vertx.core.Verticle;
import io.vertx.core.spi.VerticleFactory;
import org.springframework.stereotype.Component;

/**
 * Created on 2018/9/3 上午10:08
 *
 * @author <a href="mailto:yangkizhang@gmail.com?subject=iooo-boot">Ivan97</a>
 */
@Component
public class IoooVerticleFactory implements VerticleFactory {

	@Override
	public String prefix() {
		return VertxConfigConstants.IOOO_VERTICLE_PREFIX;
	}

	@Override
	public Verticle createVerticle(String verticleName, ClassLoader classLoader) throws Exception {
		String clazz = VerticleFactory.removePrefix(verticleName);
		return IoooVerticleServicesHolder.activeVerticleServices().rowMap().get(clazz).values().iterator().next();
	}
}
