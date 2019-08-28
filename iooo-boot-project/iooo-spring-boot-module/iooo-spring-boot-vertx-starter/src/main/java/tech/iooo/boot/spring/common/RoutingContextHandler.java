package tech.iooo.boot.spring.common;

import io.vertx.core.Handler;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.RoutingContext;
import tech.iooo.boot.core.utils.StringUtils;

/**
 * @author 龙也
 * @date 2019-08-15 19:44
 */
public interface RoutingContextHandler extends Handler<RoutingContext> {

  /**
   * handle context
   *
   * @param context RoutingContext
   */
  @Override
  void handle(RoutingContext context);

  /**
   * 默认的请求路径
   *
   * @return "/"
   */
  default String path() {
    return StringUtils.uncapitalize(this.getClass().getSimpleName());
  }

  /**
   * 默认的请求方法
   *
   * @return HttpMethod.GET
   */
  default HttpMethod httpMethod() {
    return HttpMethod.GET;
  }
}
