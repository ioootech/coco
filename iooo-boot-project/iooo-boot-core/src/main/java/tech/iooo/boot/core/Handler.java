package tech.iooo.boot.core;

/**
 * Created on 2018-12-18 23:33
 *
 * @author <a href="mailto:yangkizhang@gmail.com?subject=iooo-boot">Ivan97</a>
 */
@FunctionalInterface
public interface Handler<E> {

  void handle(E var1);
}
