package tech.iooo.boot.spring.configuration;

import io.vertx.core.Verticle;
import io.vertx.core.spi.VerticleFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Created on 2018/8/24 上午9:18
 *
 * @author <a href="mailto:yangkizhang@gmail.com?subject=iooo-spring-boot-vertx-bundle">Ivan97</a>
 */
@Component
public class SpringVerticleFactory implements VerticleFactory, ApplicationContextAware {

	private ApplicationContext applicationContext;

	@Override
	public boolean blockingCreate() {
		return true;
	}

	@Override
	public String prefix() {
		return VertxConfigConstants.IOOO_VERTICLE_PREFIX;
	}

	@Override
	public Verticle createVerticle(String verticleName, ClassLoader classLoader) throws Exception {
		String clazz = VerticleFactory.removePrefix(verticleName);
		return (Verticle) applicationContext.getBean(Class.forName(clazz));
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
}
