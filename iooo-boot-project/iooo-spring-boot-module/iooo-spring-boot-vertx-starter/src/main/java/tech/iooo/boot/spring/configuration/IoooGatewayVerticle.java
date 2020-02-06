package tech.iooo.boot.spring.configuration;

import com.google.common.collect.Table;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.ext.web.Router;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import tech.iooo.boot.core.utils.ClassUtils;
import tech.iooo.boot.core.utils.NetUtils;
import tech.iooo.boot.spring.common.RoutingContextHandler;

/**
 * @author 龙也
 * @date 2019-08-15 20:48
 */
@Slf4j
@Service
@ConditionalOnClass(Router.class)
@ConditionalOnProperty(prefix = "vertx.gateway", name = "enable", havingValue = "true")
public class IoooGatewayVerticle extends AbstractVerticle {

  @Autowired
  private IoooVertxProperties ioooVertxProperties;
  @Autowired
  @Qualifier("ioooControllerTable")
  private Table<String, HttpMethod, RoutingContextHandler> table;

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    if (table.isEmpty()) {
      logger.warn("there is no RoutingContextHandler in this application");
      startPromise.complete();
    } else {
      HttpServer server = vertx.createHttpServer(new HttpServerOptions().setLogActivity(true));
      Router router = Router.router(vertx);
      for (String path : table.rowKeySet()) {
        table.row(path).forEach((method, controller) -> {
          String configPath = path.startsWith("/") ? path : "/" + path;
          String controllerName = ClassUtils.getUserClass(controller).getSimpleName();
          logger.info("method::[{}] path::[{}] controller::[{}]", method, configPath, controllerName);
          router.route(method, configPath).handler(controller);
        });
      }
      int port = NetUtils.getAvailablePort(ioooVertxProperties.getGateway().getPort());
      server.requestHandler(router).listen(port, res -> {
        if (res.succeeded()) {
          logger.info("vertx gateway listening on port {}", port);
          startPromise.complete();
        } else {
          startPromise.fail(res.cause());
        }
      });
    }
  }
}
