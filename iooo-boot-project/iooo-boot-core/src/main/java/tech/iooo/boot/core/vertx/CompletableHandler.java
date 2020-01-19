package tech.iooo.boot.core.vertx;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import lombok.extern.slf4j.Slf4j;

/**
 * @author 龙也
 * @date 2019/12/25 5:15 下午
 */
@Slf4j
public class CompletableHandler<T> implements Handler<AsyncResult<T>> {

    private final CompletableFuture<T> completableFuture;
    private final T defaultValue;

    private CompletableHandler(T defaultValue) {
        this.completableFuture = new CompletableFuture<>();
        this.defaultValue = defaultValue;
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
            if (Objects.isNull(res.result())) {
                this.completableFuture.complete(defaultValue);
            } else {
                this.completableFuture.complete(res.result());
            }
        } else {
            this.completableFuture.completeExceptionally(res.cause());
        }
    }

    public CompletableFuture<T> completableFuture() {
        return this.completableFuture;
    }
}
