package tech.iooo.boot.example.controller;

import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.RoutingContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tech.iooo.boot.spring.annotation.RequestMapping;
import tech.iooo.boot.spring.common.RoutingContextHandler;

/**
 * @author 龙也
 * @date 2020/2/5 10:29 下午
 */
@Slf4j
@Service
@RequestMapping(path = "updateWebHook", method = "POST")
public class UpdateWebHook implements RoutingContextHandler {

  private static final String SUCCESS_RESULT = "True";

  @Override
  public void handle(RoutingContext context) {
    HttpServerRequest request = context.request();
    HttpServerResponse response = context.response();

    logger.info("uri:{}", request.uri());

    request.bodyHandler(buffer -> logger.info("body::{}", buffer.toJson()));
    response.end(SUCCESS_RESULT);
  }
}
