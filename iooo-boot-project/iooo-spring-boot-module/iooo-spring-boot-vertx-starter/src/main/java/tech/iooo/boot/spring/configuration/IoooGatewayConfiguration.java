package tech.iooo.boot.spring.configuration;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.Router;
import java.util.Map;
import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import tech.iooo.boot.spring.annotation.IoooController;
import tech.iooo.boot.spring.common.IController;

/**
 * @author 龙也
 * @date 2019-08-15 20:59
 */
@Configuration
@ConditionalOnClass(Router.class)
@ConditionalOnProperty(prefix = "vertx.server", name = "enable", havingValue = "true")
public class IoooGatewayConfiguration implements ApplicationContextAware {

  private ApplicationContext applicationContext;

  @Bean(name = "ioooControllerTable")
  public Table<String, HttpMethod, IController> loadController() {
    Map<String, Object> map = applicationContext.getBeansWithAnnotation(IoooController.class);
    //path method controller
    Table<String, HttpMethod, IController> table = HashBasedTable.create();
    map.forEach((name, controller) -> {
      IoooController ioooController = controller.getClass().getAnnotation(IoooController.class);
      table.put(ioooController.path(), ioooController.method(), (IController) controller);
    });
    return table;
  }

  @Override
  public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
  }
}
