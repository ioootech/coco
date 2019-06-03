package tech.iooo.boot.core.utils;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author yanchi.zyc
 * @date 2019-06-03
 */
class ClassUtilsTest {

  private static final Logger log = LoggerFactory.getLogger(ClassUtilsTest.class);

  @Test
  void getClassLocation() {
    log.info("{}", ClassUtils.getClassLocation(ClassUtils.class));
  }
}