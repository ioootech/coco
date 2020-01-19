package tech.iooo.boot.core.vertx;

import io.vertx.core.Future;
import java.util.concurrent.CompletableFuture;
import lombok.experimental.UtilityClass;

/**
 * @author 龙也
 * @date 2020/1/19 11:12 上午
 */
@UtilityClass
public class VertxFutureUtils {

    public <T> CompletableFuture<T> toCompletableFuture(Future<T> future) {
        CompletableHandler<T> completableHandler = CompletableHandler.init();
        future.setHandler(completableHandler);
        return completableHandler.completableFuture();
    }
}
