package tech.iooo.boot.core.utils;

import com.google.common.collect.Lists;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.ServiceLoader;
import tech.iooo.boot.core.constants.SuppressTypeConstants;

/**
 * Created on 2018-12-18 23:28
 *
 * @author <a href="mailto:yangkizhang@gmail.com?subject=iooo-boot">Ivan97</a>
 */
public class ServiceHelper {

  public static <T> T loadFactory(Class<T> clazz) {
    T factory = loadFactoryOrNull(clazz);
    if (factory == null) {
      throw new IllegalStateException("Cannot find META-INF/services/" + clazz.getName() + " on classpath");
    } else {
      return factory;
    }
  }

  public static <T> T loadFactoryOrNull(Class<T> clazz) {
    Collection<T> collection = loadFactories(clazz);
    return !collection.isEmpty() ? collection.iterator().next() : null;
  }

  public static <T> Collection<T> loadFactories(Class<T> clazz) {
    return loadFactories(clazz, (ClassLoader) null);
  }

  @SuppressWarnings(SuppressTypeConstants.UNCHECKED)
  public static <T> Collection<T> loadFactories(Class<T> clazz, ClassLoader classLoader) {
    List<T> list = Lists.newArrayList();
    ServiceLoader factories;
    if (classLoader != null) {
      factories = ServiceLoader.load(clazz, classLoader);
    } else {
      factories = ServiceLoader.load(clazz);
    }

    if (factories.iterator().hasNext()) {
      factories.iterator().forEachRemaining(factory -> list.add(((T) factory)));
      return list;
    } else {
      factories = ServiceLoader.load(clazz, ServiceHelper.class.getClassLoader());
      if (factories.iterator().hasNext()) {
        factories.iterator().forEachRemaining(factory -> list.add(((T) factory)));
        return list;
      } else {
        return Collections.emptyList();
      }
    }
  }
}
