package tech.iooo.boot.core.constants;

import org.junit.jupiter.api.Test;

/**
 * Created on 2018-12-18 10:36
 *
 * @author <a href="mailto:yangkizhang@gmail.com?subject=iooo-boot">Ivan97</a>
 */
class SystemPropertiesTest {

  @Test
  void test() {
    System.out.println(SystemProperties.JAVA_VM_VERSION);
    System.out.println(SystemProperties.OS_NAME);
    System.out.println(SystemProperties.FILE_ENCODING);
    System.out.println(SystemProperties.FILE_SEPARATOR);
  }
}
