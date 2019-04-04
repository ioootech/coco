package tech.iooo.boot.cache;

import java.util.Collections;
import java.util.Set;
import lombok.Data;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Created on 2019-03-26 15:45
 *
 * @author <a href="mailto:yangkizhang@gmail.com?subject=iooo-boot">Ivan97</a>
 */
@Data
public class ClusterRedisConfig implements Config {

  private static final long serialVersionUID = -6024139246693242553L;
  private Set<HostAndPort> cluster = Collections.singleton(new HostAndPort("localhost", 6379));
  private String password;
  private int timeOut = 120;
  private int soTimeout = 120;
  private int maxAttempts = 6;
  private int maxTotal = 16;
  private int maxIdle = 8;

  private JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();

  {
    jedisPoolConfig.setMaxIdle(maxIdle);
    jedisPoolConfig.setMaxTotal(maxTotal);
  }
}
