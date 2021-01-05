package tech.iooo.boot.example.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import tech.iooo.boot.core.utils.SocketUtils;
import tech.iooo.boot.example.service.Greeter;

/**
 * Created on 2018/8/24 下午2:51
 *
 * @author <a href="mailto:yangkizhang@gmail.com?subject=iooo-boot">Ivan97</a>
 */
public class GreetingVerticle extends AbstractVerticle {

  private static final Logger logger = LoggerFactory.getLogger(GreetingVerticle.class);
  @Autowired
  private Greeter greeter;

  private final int port = SocketUtils.findAvailableTcpPort();

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    vertx.createHttpServer().requestHandler(request -> {
      String name = request.getParam("name");
      logger.info("Got request for namePrefix: " + name);
      if (name == null) {
        request.response().setStatusCode(400).end("Missing namePrefix");
      } else {
        // It's fine to call the greeter from the event loop as it's not blocking
        request.response().end(greeter.sayHello(name));
      }
    }).listen(port, ar -> {
      if (ar.succeeded()) {
        logger.info("listening on port {}", port);
        startPromise.complete();
      } else {
        startPromise.fail(ar.cause());
      }
    });
  }
}
