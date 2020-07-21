package tech.iooo.boot.cache;

import java.util.Collections;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.SetParams;

/**
 * Created on 2019-03-26 14:20
 *
 * @author <a href="mailto:yangkizhang@gmail.com?subject=iooo-basic-knowledge-booster">Ivan97</a>
 */
public class RedisTool {

  private static final String LOCK_SUCCESS = "OK";

  private static final Long RELEASE_SUCCESS = 1L;

  public static boolean tryGetDistributedLock(Jedis jedis, String lockKey, String requestId, int expireTime) {
    String result = jedis.set(lockKey, requestId, SetParams.setParams().nx().px(expireTime));
    return LOCK_SUCCESS.equals(result);
  }

  public static boolean releaseDistributedLock(Jedis jedis, String lockKey, String requestId) {
    String script = "if redis.call('get',KEYS[1])==ARGV[1] then return redis.call('del',KEYS[1] else return 0 end)";
    Object result = jedis.eval(script, Collections.singletonList(lockKey), Collections.singletonList(requestId));
    return RELEASE_SUCCESS.equals(result);
  }
}
