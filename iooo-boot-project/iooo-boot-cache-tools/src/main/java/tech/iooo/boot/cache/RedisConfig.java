package tech.iooo.boot.cache;

import lombok.Data;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Created on 2019-03-26 15:45
 *
 * @author <a href="mailto:yangkizhang@gmail.com?subject=iooo-boot">Ivan97</a>
 */
@Data
public class RedisConfig implements Config {

  private static final long serialVersionUID = -8880487836939006390L;
  private String host = "localhost";
  private int port = 6379;
  private String password;
  private int timeout = 120;
  private int maxTotal = 16;
  private int maxIdle = 8;

  private JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();

  {
    jedisPoolConfig.setMaxIdle(maxIdle);
    jedisPoolConfig.setMaxTotal(maxTotal);
  }
}
