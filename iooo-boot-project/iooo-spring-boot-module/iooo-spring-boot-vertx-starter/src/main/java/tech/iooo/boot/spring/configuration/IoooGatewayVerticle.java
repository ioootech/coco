package tech.iooo.boot.spring.configuration;

import com.google.common.collect.Table;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import tech.iooo.boot.spring.common.IController;

/**
 * @author 龙也
 * @date 2019-08-15 20:48
 */
@Service
@ConditionalOnClass(Router.class)
@ConditionalOnProperty(prefix = "vertx.server", name = "enable", havingValue = "true")
public class IoooGatewayVerticle extends AbstractVerticle {

  private static final Logger log = LoggerFactory.getLogger(IoooGatewayVerticle.class);

  @Autowired
  private IoooVertxProperties ioooVertxProperties;
  @Autowired
  @Qualifier("ioooControllerTable")
  private Table<String, HttpMethod, IController> table;

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    HttpServer server = vertx.createHttpServer();
    Router router = Router.router(vertx);
    for (String path : table.rowKeySet()) {
      table.row(path).forEach((method, controller) -> {
        log.info("method::[{}] path::[{}] controller::[{}]", method, path, controller.getClass().getSimpleName());
        router.route(method, path).handler(controller);
      });
    }

    server.requestHandler(router).listen(ioooVertxProperties.getServer().getPort(), res -> {
      if (res.succeeded()) {
        log.info("vertx gateway listening on port {}", ioooVertxProperties.getServer().getPort());
        startPromise.complete();
      } else {
        startPromise.fail(res.cause());
      }
    });
  }
}
