package tech.iooo.boot.example.cofiguration;

import io.vertx.core.DeploymentOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created on 2018/8/24 下午3:12
 *
 * @author <a href="mailto:yangkizhang@gmail.com?subject=iooo-boot">Ivan97</a>
 */
@Configuration
public class VerticleConfiguration {

  @Bean("greeterDeploymentOptions")
  public DeploymentOptions greeterDeploymentOptions() {
    return new DeploymentOptions().setInstances(4);
  }
}
