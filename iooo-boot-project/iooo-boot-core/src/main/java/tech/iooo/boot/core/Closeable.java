package tech.iooo.boot.core;

/**
 * Created on 2018-12-18 23:35
 *
 * @author <a href="mailto:yangkizhang@gmail.com?subject=iooo-boot">Ivan97</a>
 */
public interface Closeable {

  void close(Handler<AsyncResult<Void>> res);
}
