package tech.iooo.boot.cache;

import redis.clients.jedis.JedisPool;
import tech.iooo.boot.core.Lifecycle;
import tech.iooo.boot.core.utils.Assert;

/**
 * Created on 2019-03-26 22:14
 *
 * @author <a href="mailto:yangkizhang@gmail.com?subject=iooo-boot">Ivan97</a>
 */
public class JedisSentinelLifecycle implements Lifecycle {

  private final CacheType cacheType;

  private Config config;
  private boolean running;
  private JedisPool pool;

  public JedisSentinelLifecycle(CacheType cacheType, Config initConfig) {
    this.cacheType = cacheType;
    switch (cacheType) {
      case JedisCluster:
        Assert.isTrue(initConfig instanceof ClusterRedisConfig, "config type error");
        config = initConfig;
        break;
      case Jedis:
        Assert.isTrue(initConfig instanceof RedisConfig, "config type error");
        config = initConfig;
      default:
        break;
    }
  }

  @Override
  public void start() {
    switch (cacheType) {
      case Jedis: {
        RedisConfig redisConfig = (RedisConfig) config;
        pool = new JedisPool(redisConfig.getJedisPoolConfig(), redisConfig.getHost(), redisConfig.getPort(), redisConfig.getTimeout(), redisConfig.getPassword());
        JedisSentinelKit.init(pool);

        CacheKit.init(new JedisCacheable());
        break;
      }
      case JedisCluster: {
        JedisClusterCacheable jedisClusterCacheable = new JedisClusterCacheable();
        jedisClusterCacheable.init((ClusterRedisConfig) config);
        CacheKit.init(jedisClusterCacheable);
        break;
      }
      default:
        break;

    }
    this.running = true;
  }

  @Override
  public void stop() {
    try {
      pool.destroy();
      JedisSentinelKit.init(null);
    } catch (Exception ignored) {
    }
    this.running = false;
  }

  @Override
  public boolean isRunning() {
    return this.running;
  }
}
