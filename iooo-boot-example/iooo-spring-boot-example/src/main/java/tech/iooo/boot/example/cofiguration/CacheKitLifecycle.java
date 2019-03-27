package tech.iooo.boot.example.cofiguration;

import org.springframework.context.SmartLifecycle;
import org.springframework.stereotype.Component;
import tech.iooo.boot.cache.CacheType;
import tech.iooo.boot.cache.JedisSentinelLifecycle;
import tech.iooo.boot.cache.RedisConfig;

/**
 * Created on 2019-03-27 11:06
 *
 * @author <a href="mailto:yangkizhang@gmail.com?subject=iooo-boot">Ivan97</a>
 */
@Component
public class CacheKitLifecycle implements SmartLifecycle {

  private JedisSentinelLifecycle jedisSentinelLifecycle = new JedisSentinelLifecycle(CacheType.Jedis, new RedisConfig());

  @Override
  public void start() {
    jedisSentinelLifecycle.start();
  }

  @Override
  public void stop() {
    jedisSentinelLifecycle.stop();
  }

  @Override
  public boolean isRunning() {
    return jedisSentinelLifecycle.isRunning();
  }
}
