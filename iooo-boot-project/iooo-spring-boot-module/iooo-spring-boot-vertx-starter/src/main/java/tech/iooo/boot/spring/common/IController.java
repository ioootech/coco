package tech.iooo.boot.spring.common;

import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;

/**
 * @author 龙也
 * @date 2019-08-15 19:44
 */
public interface IController extends Handler<RoutingContext> {

    /**
     * handle context
     *
     * @param context RoutingContext
     */
    @Override
    void handle(RoutingContext context);
}
