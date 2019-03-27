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

  public JedisSentinelLifecycle(CacheType cacheType) {
    this.cacheType = cacheType;
    switch (cacheType) {
      case JedisCluster:
        config = new ClusterRedisConfig();
        break;
      case Jedis:
        config = new RedisConfig();
      default:
        break;
    }
  }

  public JedisSentinelLifecycle(Config config, CacheType cacheType) {
    this.config = config;
    this.cacheType = cacheType;
  }

  @Override
  public void start() {
    switch (cacheType) {
      case Jedis: {
        Assert.isTrue(config instanceof RedisConfig, "config type error");
        RedisConfig redisConfig = (RedisConfig) config;
        pool = new JedisPool(redisConfig.getJedisPoolConfig(), redisConfig.getHost(), redisConfig.getPort(), redisConfig.getTimeout(), redisConfig.getPassword());
        JedisSentinelKit.init(pool);

        CacheKit.init(new JedisCacheable());
        break;
      }
      case JedisCluster: {
        Assert.isTrue(config instanceof ClusterRedisConfig, "config type error");
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
