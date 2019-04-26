package tech.iooo.boot.core.spring.cache;

import com.google.common.collect.Maps;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import tech.iooo.boot.core.constants.SuppressTypeConstants;

/**
 * Created on 2018-12-10 14:03
 *
 * @author <a href="mailto:yangkizhang@gmail.com?subject=iooo-boot">Ivan97</a>
 */
public class CacheMethodInterceptor implements MethodInterceptor {

  /**
   * 代理对象
   */
  private Object target;
  /**
   * 缓存
   */
  private Map<String, CacheWrapper<Object>> cacheWrapper = Maps.newHashMap();

  public CacheMethodInterceptor() {
  }

  public CacheMethodInterceptor(Object target) {
    this.target = target;
  }


  @Override
  @SuppressWarnings(SuppressTypeConstants.UNCHECKED)
  public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
    Object result = null;
    CacheWrapper wrapper = null;
    Cache cache = method.getAnnotation(Cache.class);
    if (!( Objects.isNull(cache) || cache.timeInterval() <= 0)) {
      if (cacheWrapper.containsKey(method.toString())) {
        wrapper = cacheWrapper.get(method.toString());
      } else {
        wrapper = new CacheWrapper(cache);
        result = method.invoke(target, args);
        wrapper.setCacheValue(result);
        cacheWrapper.put(method.toString(), wrapper);
      }

      if (wrapper.isTimeOut()) {
        //缓存过期
        synchronized (wrapper) {
          if (wrapper.isTimeOut()) {
            wrapper.resetTimer();
            result = method.invoke(target, args);
            wrapper.setCacheValue(result);
          } else {
            result = wrapper.getCacheValue();
          }
        }
      } else {
        result = wrapper.getCacheValue();
      }

    } else {
      //如果不需求缓存，即没有Cache注解
      result = method.invoke(target, args);
    }
    return result;
  }
}
