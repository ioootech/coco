package tech.iooo.boot.core;

/**
 * A generic event handler.
 *
 * Created on 2018-12-18 23:33
 *
 * @author <a href="mailto:yangkizhang@gmail.com?subject=iooo-boot">Ivan97</a>
 */
@FunctionalInterface
public interface Handler<E> {

  /**
   * Something has happened, so handle it.
   *
   * @param event the event to handle
   */
  void handle(E event);
}
