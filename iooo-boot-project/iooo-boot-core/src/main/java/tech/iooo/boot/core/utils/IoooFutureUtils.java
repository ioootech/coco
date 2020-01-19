package tech.iooo.boot.core.utils;

import java.util.concurrent.CompletableFuture;
import lombok.experimental.UtilityClass;
import tech.iooo.boot.core.Future;
import tech.iooo.boot.core.Promise;

/**
 * @author 龙也
 * @date 2020/1/19 11:12 上午
 */
@UtilityClass
public class IoooFutureUtils {

  public <T> CompletableFuture<T> toCompletableFuture(Future<T> future) {
    CompletableHandler<T> completableHandler = CompletableHandler.init();
    future.setHandler(completableHandler);
    return completableHandler.completableFuture();
  }

  public <T> CompletableFuture<T> toCompletableFuture(Promise<T> promise) {
    return toCompletableFuture(promise.future());
  }
}
