package tech.iooo.boot.cache;

/**
 * Created on 2019-03-26 15:23
 *
 * @author <a href="mailto:yangkizhang@gmail.com?subject=iooo-boot">Ivan97</a>
 */
public interface JedisMessage<T> {

  void onMessage(T message);
}
