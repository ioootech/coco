package tech.iooo.boot.core.impl;

import java.util.concurrent.CompletableFuture;
import lombok.extern.slf4j.Slf4j;
import tech.iooo.boot.core.AsyncResult;
import tech.iooo.boot.core.Handler;

/**
 * @author 龙也
 * @date 2019/12/25 5:15 下午
 */
@Slf4j
public class CompletableHandler<T> implements Handler<AsyncResult<T>> {

  private final CompletableFuture<T> completableFuture;
  private final T defaultValue;

  private CompletableHandler(T defaultValue) {
    this.defaultValue = defaultValue;
    this.completableFuture = new CompletableFuture<>();
  }

  public static <T> CompletableHandler<T> init() {
    return new CompletableHandler<>(null);
  }

  public static <T> CompletableHandler<T> init(T defaultValue) {
    return new CompletableHandler<>(defaultValue);
  }

  @Override
  public void handle(AsyncResult<T> res) {
    if (res.succeeded()) {
      this.completableFuture.complete(res.result());
    } else {
      res.cause().printStackTrace();
      this.completableFuture.complete(defaultValue);
    }
  }

  public CompletableFuture<T> completableFuture() {
    return this.completableFuture;
  }
}
