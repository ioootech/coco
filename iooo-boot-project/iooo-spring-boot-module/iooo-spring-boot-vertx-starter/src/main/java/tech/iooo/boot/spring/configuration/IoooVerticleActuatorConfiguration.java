package tech.iooo.boot.spring.configuration;

import org.springframework.boot.actuate.autoconfigure.endpoint.EndpointAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tech.iooo.boot.spring.endpoint.IoooDeployVerticleEndpoint;
import tech.iooo.boot.spring.endpoint.IoooListVerticleEndpoint;
import tech.iooo.boot.spring.endpoint.IoooUndeployVerticleEndpoint;

/**
 * Created on 2018/9/12 下午2:53
 *
 * @author <a href="mailto:yangkizhang@gmail.com?subject=iooo-boot">Ivan97</a>
 */
@Configuration
@ConditionalOnBean(EndpointAutoConfiguration.class)
public class IoooVerticleActuatorConfiguration {

	@Bean
	public IoooListVerticleEndpoint ioooListVerticleEndpoint() {
		return new IoooListVerticleEndpoint();
	}

	@Bean
	public IoooUndeployVerticleEndpoint ioooUndeployVerticleEndpoint() {
		return new IoooUndeployVerticleEndpoint();
	}
	
	@Bean
	public IoooDeployVerticleEndpoint ioooDeployVerticleEndpoint(){
		return new IoooDeployVerticleEndpoint();
	}
}
