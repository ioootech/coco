package tech.iooo.boot.core;

/**
 * Created on 2019-03-26 22:47
 *
 * @author <a href="mailto:yangkizhang@gmail.com?subject=iooo-boot">Ivan97</a>
 */
public interface Lifecycle {

  int DEFAULT_PHASE = Integer.MAX_VALUE;

  void start();

  void stop();

  boolean isRunning();

  default int getPhase() {
    return DEFAULT_PHASE;
  }

  default boolean isAutoStartup() {
    return true;
  }

  default void stop(Runnable callback) {
    this.stop();
    callback.run();
  }
}
