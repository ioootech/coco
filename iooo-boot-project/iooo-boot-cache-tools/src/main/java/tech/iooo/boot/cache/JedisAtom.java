package tech.iooo.boot.cache;

import redis.clients.jedis.Transaction;

/**
 * Created on 2019-03-26 15:22
 *
 * @author <a href="mailto:yangkizhang@gmail.com?subject=iooo-boot">Ivan97</a>
 */
public interface JedisAtom {

  void action(Transaction transaction);
}
