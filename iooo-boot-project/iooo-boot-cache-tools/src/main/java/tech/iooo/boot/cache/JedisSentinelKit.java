/*******************************************************************************
 * Copyright (c) 2014-6-26 @author <a href="mailto:iffiff1@hotmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@hotmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package tech.iooo.boot.cache;

import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;
import redis.clients.jedis.Tuple;
import redis.clients.util.Pool;
import redis.clients.util.SafeEncoder;
import tech.iooo.boot.cache.utils.KryoRedisSerializer;
import tech.iooo.boot.core.constants.SuppressTypeConstants;
import tech.iooo.boot.core.logging.Logger;
import tech.iooo.boot.core.logging.LoggerFactory;

/**
 * @author Ivan97
 */
@SuppressWarnings(SuppressTypeConstants.ALL)
public class JedisSentinelKit {

  private static final Logger logger = LoggerFactory.getLogger(JedisSentinelKit.class);
  private static final String SUCCESS = "OK";
  private static Pool<Jedis> pool;

  /**
   * must invoke this method to set the pool before use this kit.
   */
  public static void init(Pool<Jedis> pool) {
    JedisSentinelKit.pool = pool;
  }

  public static List<Object> tx(JedisAtom jedisAtom) {
    Jedis jedis = pool.getResource();
    Transaction trans = jedis.multi();
    jedisAtom.action(trans);
    return trans.exec();

  }

  public static <T> T call(JedisAction<T> jedisAction) {
    T result = null;
    Jedis jedis = pool.getResource();
    try {
      result = jedisAction.action(jedis);
    } catch (Exception e) {
      logger.error("[JedisAction.action(Jedis)]", e);
      if (null != jedis) {
        pool.returnBrokenResource(jedis);
      }
    } finally {
      if (null != jedis) {
        pool.returnResource(jedis);
      }
    }
    return result;
  }

  /***
   * query one Object from Redis with key
   * @param key using it get value from key-value database
   * @return the Object which implements Serializable
   */

  public static <T> T get(final String key) {
    return call(jedis -> {
      Object result = null;
      byte[] retVal = jedis.get(SafeEncoder.encode(key));
      if (null != retVal) {
        try {
          result = KryoRedisSerializer.deserialize(retVal);
        } catch (Exception e) {
          result = SafeEncoder.encode(retVal);
        }
      }
      return (T) result;
    });
  }

  public static Long ttl(final String key) {
    return call(jedis -> jedis.ttl(SafeEncoder.encode(key)));
  }

  public static Long expire(final String key, final int seconds) {
    return call(jedis -> jedis.expire(SafeEncoder.encode(key), seconds));
  }

  /***
   * set object to key-value database with the specified key
   *
   * @param key
   *            the unique key to indicate the value Object
   * @param value
   *            the value indicated by the key
   * @return return true while the set operation is succeed,false by failed
   */

  public static boolean set(final String key, final Object value) {
    return call(jedis -> {
      String retVal;
      if (value instanceof String) {
        retVal = jedis.set(key, (String) value);
      } else {
        retVal = jedis.set(SafeEncoder.encode(key),
            KryoRedisSerializer.serialize(value));
      }
      return SUCCESS.equalsIgnoreCase(retVal);
    });
  }

  /***
   * set object to key-value database with the specified key and EXPIRE time
   *
   * @param key
   *            the unique key to indicate the value Object
   * @param value
   *            the value indicated by the key
   * @param seconds
   *            EXPIRE time
   * @return return true while the set operation is succeed,false by failed
   */

  public static boolean set(final String key, final Object value, final int seconds) {
    return call(jedis -> {
      byte[] bytes;
      if (value instanceof String) {
        bytes = SafeEncoder.encode((String) value);
      } else {
        bytes = KryoRedisSerializer.serialize(value);
      }
      String retVal = jedis.setex(SafeEncoder.encode(key), seconds, bytes);
      return SUCCESS.equalsIgnoreCase(retVal);
    });
  }

  /***
   * query multiple Object from Redis with key
   *
   * @param keys
   *            using it get value from key-value database
   * @return the Object list which implements Serializable
   */

  public static List mquery(final String... keys) {

    return call(jedis -> {
      List result = Lists.newArrayListWithCapacity(keys.length);
      for (int index = 0; index < keys.length; index++) {
        result.add(null);
      }
      byte[][] encodeKeys = new byte[keys.length][];
      for (int i = 0; i < keys.length; i++) {
        encodeKeys[i] = SafeEncoder.encode(keys[i]);
      }
      List<byte[]> retVals = jedis.mget(encodeKeys);
      if (null != retVals) {
        int index = 0;
        for (byte[] val : retVals) {
          if (null != val) {
            result.set(index, KryoRedisSerializer.deserialize(val));
          }
          index++;
        }
      }
      return result;
    });
  }

  public static List<String> mqueryStr(final String... keys) {

    return call(jedis -> jedis.mget(keys));
  }

  public static boolean msaveOrUpdate(final Map<String, Object> values) {
    return call(jedis -> {
      byte[][] encodeValues = new byte[values.size() * 2][];
      int index = 0;
      for (Entry<String, Object> entry : values.entrySet()) {
        encodeValues[index++] = entry.getKey().getBytes();
        encodeValues[index++] = KryoRedisSerializer.serialize(entry.getValue());
      }
      String retVal = jedis.mset(encodeValues);
      return SUCCESS.equalsIgnoreCase(retVal);
    });
  }

  public static boolean msaveOrUpdateStr(final Map<String, String> values) {

    return call(jedis -> {
      Iterator<Entry<String, String>> iter = values.entrySet().iterator();
      int index = 0;
      String[] encodeValues = new String[values.size() * 2];
      while (iter.hasNext()) {
        Entry<String, String> entry = iter.next();
        encodeValues[index++] = entry.getKey();
        encodeValues[index++] = entry.getValue();
      }
      return SUCCESS.equalsIgnoreCase(jedis.mset(encodeValues));
    });
  }

  /**
   * query keys set by pattern
   */

  public static Set<String> keys(final String pattern) {
    return call(jedis -> jedis.keys(pattern));

  }

  public static long del(final String... keys) {
    return call(jedis -> {
      byte[][] encodeKeys = new byte[keys.length][];
      for (int i = 0; i < keys.length; i++) {
        encodeKeys[i] = SafeEncoder.encode(keys[i]);
      }
      return jedis.del(encodeKeys);
    });
  }

  public static long listAdd(final String key, final Object value) {
    return call(jedis -> jedis.rpush(SafeEncoder.encode(key), KryoRedisSerializer.serialize(value)));
  }

  public static long listAddFirst(final String key, final Object value) {
    return call(jedis -> jedis.lpush(SafeEncoder.encode(key), KryoRedisSerializer.serialize(value)));
  }

  public static String type(final String key) {
    return call(jedis -> jedis.type(SafeEncoder.encode(key)));
  }

  public static List queryList(final String key, final int start, final int end) {
    return call(jedis -> {
      List result = Lists.newArrayList();
      List<byte[]> retVals = jedis.lrange(SafeEncoder.encode(key),
          start, end);
      if (retVals != null) {
        for (byte[] val : retVals) {
          if (null != val) {
            result.add(KryoRedisSerializer.deserialize(val));
          }
        }
      }
      return result;
    });
  }

  public static long listSize(final String key) {
    return call(jedis -> jedis.llen(SafeEncoder.encode(key)));
  }

  public static boolean listTrim(final String key, final int start, final int end) {
    return call(jedis -> SUCCESS.equalsIgnoreCase(jedis.ltrim(SafeEncoder.encode(key), start, end)));
  }

  public static long incrementAndGet(final String key) {
    return call(jedis -> jedis.incr(key));
  }

  public static long decrementAndGet(final String key) {
    return call(jedis -> jedis.decr(key));
  }

  public static long queryLong(final String key) {
    return call(jedis -> Long.valueOf(jedis.get(key)));
  }

  public static boolean hmset(final String key, final Map<String, String> values) {
    return call(jedis -> SUCCESS.equals(jedis.hmset(key, values)));
  }

  public static List<String> hvals(final String key) {
    return call(jedis -> jedis.hvals(key));
  }

  public static boolean exists(final String key) {
    return call(jedis -> jedis.exists(key));
  }

  public static List<String> hmget(final String key, final String... fields) {
    return call(jedis -> jedis.hmget(key, fields));
  }

  public static Double zincrby(final String key, final double score, final String member) {
    return call(jedis -> jedis.zincrby(key, score, member));
  }

  public static Double zscore(final String key, final String score) {
    return call(jedis -> jedis.zscore(key, score));
  }

  public static Long zadd(final String key, final double score, final String member) {
    return call(jedis -> jedis.zadd(key, score, member));
  }

  public static Set<Tuple> zrangeWithScores(final String key, final long start, final long end) {
    return call(jedis -> jedis.zrangeWithScores(key, start, end));
  }

  public static String watch(final String... keys) {
    return call(jedis -> jedis.watch(keys));
  }

  public static Long lpush(final String key, final Object value) {
    return call(jedis -> {
      Long retVal;
      if (value instanceof String) {
        retVal = jedis.lpush(key, (String) value);
      } else {
        retVal = jedis.lpush(SafeEncoder.encode(key),
            KryoRedisSerializer.serialize(value));
      }
      return retVal;
    });
  }

  public static <T> T rpop(final String key) {
    return call(jedis -> {
      Object result = null;
      byte[] retVal = jedis.rpop(SafeEncoder.encode(key));
      if (null != retVal) {
        try {
          result = KryoRedisSerializer.deserialize(retVal);
        } catch (Exception e) {
          result = SafeEncoder.encode(retVal);
        }
      }
      return (T) result;
    });
  }

  public static List lrange(final String key, final long start, final long end) {
    return call(jedis -> {
      List list = Lists.newArrayList();
      byte[] result = jedis.lpop(SafeEncoder.encode(key));
      if (result == null) {
        return null;
      }
      try {
        list.add(KryoRedisSerializer.deserialize(result));
      } catch (Exception e) {
        list.add(SafeEncoder.encode(result));
      }
      return list;
    });
  }

  public static <T> T rpoplpush(final String srckey, final String dstkey) {
    return call(jedis -> {
      Object result = null;
      byte[] retVal = jedis.rpoplpush(SafeEncoder.encode(srckey),
          SafeEncoder.encode(dstkey));
      if (null != retVal) {
        try {
          result = KryoRedisSerializer.deserialize(retVal);
        } catch (Exception e) {
          result = SafeEncoder.encode(retVal);
        }
      }
      return (T) result;
    });
  }

  public static Long lrem(String key, Object value) {
    return lrem(key, 1, value);
  }

  public static Long lrem(final String key, final long count, final Object value) {
    return call(jedis -> {
      Long retVal;
      if (value instanceof String) {
        retVal = jedis.lrem(key, count, (String) value);
      } else {
        retVal = jedis.lrem(SafeEncoder.encode(key), count,
            KryoRedisSerializer.serialize(value));
      }
      return retVal;
    });
  }
}
