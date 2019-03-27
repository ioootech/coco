package tech.iooo.boot.cache;

import redis.clients.jedis.Jedis;

/**
 * Created on 2019-03-26 15:21
 *
 * @author <a href="mailto:yangkizhang@gmail.com?subject=iooo-boot">Ivan97</a>
 */
public interface JedisAction<T> {

  T action(Jedis jedis);
}
