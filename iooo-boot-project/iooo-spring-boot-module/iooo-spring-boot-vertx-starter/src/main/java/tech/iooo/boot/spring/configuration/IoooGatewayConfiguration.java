package tech.iooo.boot.spring.configuration;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.Router;
import java.util.Map;
import java.util.Objects;
import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import tech.iooo.boot.core.utils.Assert;
import tech.iooo.boot.spring.annotation.RequestMapping;
import tech.iooo.boot.spring.common.RoutingContextHandler;

/**
 * @author 龙也
 * @date 2019-08-15 20:59
 */
@Configuration
@ConditionalOnClass(Router.class)
public class IoooGatewayConfiguration implements ApplicationContextAware {

  private ApplicationContext applicationContext;

  @Bean(name = "ioooControllerTable")
  public Table<String, HttpMethod, RoutingContextHandler> loadController() {
    Map<String, RoutingContextHandler> map = applicationContext.getBeansOfType(RoutingContextHandler.class);
    //path method controller
    Table<String, HttpMethod, RoutingContextHandler> table = HashBasedTable.create();
    map.forEach((name, controller) -> {
      RequestMapping requestMapping = controller.getClass().getAnnotation(RequestMapping.class);
      //优先看RequestMapping配置
      if (Objects.nonNull(requestMapping)) {
        String configPath = requestMapping.path().trim();
        Assert.doesNotContain(configPath, " ", "path参数不应该包含空格");
        String path = configPath.startsWith("/") ? configPath : "/" + configPath;
        table.put(path, requestMapping.method(), controller);
      } else {
        table.put(controller.path(), controller.httpMethod(), controller);
      }
    });
    return table;
  }

  @Override
  public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
  }
}
