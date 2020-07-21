package tech.iooo.boot.cache;

import com.google.common.collect.Sets;
import java.util.Map;
import java.util.Set;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.util.SafeEncoder;
import tech.iooo.boot.cache.utils.KryoRedisSerializer;

/**
 * Created on 2019-03-26 15:59
 *
 * @author <a href="mailto:yangkizhang@gmail.com?subject=iooo-boot">Ivan97</a>
 */
public class JedisClusterCacheable<T> implements Cacheable<T> {

  private JedisCluster jedisCluster;

  public void init(ClusterRedisConfig clusterRedisConfig) {
    jedisCluster = new JedisCluster(clusterRedisConfig.getCluster(), clusterRedisConfig.getTimeOut(), clusterRedisConfig.getMaxAttempts(), clusterRedisConfig.getJedisPoolConfig());
  }

  @Override
  public T get(String key) {
    Object result = null;
    byte[] retVal = jedisCluster.get(SafeEncoder.encode(key));
    if (null != retVal) {
      try {
        result = KryoRedisSerializer.deserialize(retVal);
      } catch (Exception e) {
        result = SafeEncoder.encode(retVal);
      }
    }
    return (T) result;
  }

  @Override
  public Set<String> keys(String pattern) {
    Set<String> keys = Sets.newHashSet();
    Map<String, JedisPool> clusterNodes = jedisCluster.getClusterNodes();
    for (String k : clusterNodes.keySet()) {
      JedisPool jp = clusterNodes.get(k);
      Jedis jedis = jp.getResource();
      keys.addAll(jedis.keys(pattern));
      jedis.close();
    }
    return keys;
  }

  @Override
  public boolean set(String key, T value) {
    String retVal;
    if (value instanceof String) {
      retVal = jedisCluster.set(key, (String) value);
    } else {
      retVal = jedisCluster.set(SafeEncoder.encode(key), KryoRedisSerializer.serialize(value));
    }
    return true;
  }

  @Override
  public boolean set(String key, T value, int timeout) {
    String retVal;
    if (value instanceof String) {
      retVal = jedisCluster.setex(key, timeout, (String) value);
    } else {
      retVal = jedisCluster.setex(SafeEncoder.encode(key), timeout, KryoRedisSerializer.serialize(value));
    }
    return true;
  }

  @Override
  public long del(String key) {
    return jedisCluster.del(SafeEncoder.encode(key));
  }

  @Override
  public Long ttl(String key) {
    return jedisCluster.ttl(SafeEncoder.encode(key));
  }

  @Override
  public boolean hasKey(String key) {
    return jedisCluster.exists(key);
  }

  @Override
  public Long expire(String key, int seconds) {
    return jedisCluster.expire(SafeEncoder.encode(key), seconds);
  }

  @Override
  public void delAll(String pattern) {
    keys(pattern).forEach(this::del);
  }
}
