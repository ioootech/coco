package tech.iooo.boot.core;

import java.util.function.Function;

/**
 * Created on 2018-12-18 23:33
 *
 * @author <a href="mailto:yangkizhang@gmail.com?subject=iooo-boot">Ivan97</a>
 */
public interface AsyncResult<T> {

  T result();

  Throwable cause();

  boolean succeeded();

  boolean failed();

  default <U> AsyncResult<U> map(final Function<T, U> mapper) {
    if (mapper == null) {
      throw new NullPointerException();
    } else {
      return new AsyncResult<U>() {
        @Override
        public U result() {
          return this.succeeded() ? mapper.apply(AsyncResult.this.result()) : null;
        }

        @Override
        public Throwable cause() {
          return AsyncResult.this.cause();
        }

        @Override
        public boolean succeeded() {
          return AsyncResult.this.succeeded();
        }

        @Override
        public boolean failed() {
          return AsyncResult.this.failed();
        }
      };
    }
  }

  default <V> AsyncResult<V> map(V value) {
    return this.map((t) -> {
      return value;
    });
  }

  default <V> AsyncResult<V> mapEmpty() {
    return (AsyncResult<V>) this.map((Object) null);
  }

  default AsyncResult<T> otherwise(final Function<Throwable, T> mapper) {
    if (mapper == null) {
      throw new NullPointerException();
    } else {
      return new AsyncResult<T>() {
        @Override
        public T result() {
          if (AsyncResult.this.succeeded()) {
            return AsyncResult.this.result();
          } else {
            return AsyncResult.this.failed() ? mapper.apply(AsyncResult.this.cause()) : null;
          }
        }

        @Override
        public Throwable cause() {
          return null;
        }

        @Override
        public boolean succeeded() {
          return AsyncResult.this.succeeded() || AsyncResult.this.failed();
        }

        @Override
        public boolean failed() {
          return false;
        }
      };
    }
  }

  default AsyncResult<T> otherwise(T value) {
    return this.otherwise((err) -> value);
  }

  default AsyncResult<T> otherwiseEmpty() {
    return this.otherwise((err) -> null);
  }
}

