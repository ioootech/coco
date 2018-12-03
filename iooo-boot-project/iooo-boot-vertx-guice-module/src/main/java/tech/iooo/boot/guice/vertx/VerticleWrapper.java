package tech.iooo.boot.guice.vertx;

import io.vertx.core.Verticle;
import java.lang.reflect.Method;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created on 2018-12-03 09:31
 *
 * @author <a href="mailto:yangkizhang@gmail.com?subject=iooo-boot">Ivan97</a>
 */
public class VerticleWrapper<T extends Verticle> implements MethodInterceptor {

  private static final Logger logger = LoggerFactory.getLogger(VerticleWrapper.class);
  private final T verticle;

  public VerticleWrapper(T verticle) {
    this.verticle = verticle;
  }

  @Override
  public Object intercept(Object o, Method method, Object[] args, MethodProxy proxy) throws Throwable {
    Object result;
    if (logger.isDebugEnabled()) {
      logger.debug("Interceptor begin");
    }
    result = proxy.invoke(o, args);
    if (logger.isDebugEnabled()) {
      logger.debug("Interceptor end");
    }
    return result;
  }
}
