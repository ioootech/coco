package tech.iooo.boot.core.utils;

import org.junit.jupiter.api.Test;

/**
 * Created on 2018-12-27 15:12
 *
 * @author <a href="mailto:yangkizhang@gmail.com?subject=iooo-boot">Ivan97</a>
 */
class IPUtilsTest {

  @Test
  void isInRange() {
    System.out.println(IPUtils.isInRange("10.153.48.127", "10.153.48.127/0"));
    System.out.println(IPUtils.isInRange("10.153.48.127", "10.153.48.0/26"));
    System.out.println(IPUtils.isInRange("10.168.1.2", "10.168.0.224/23"));
    System.out.println(IPUtils.isInRange("192.168.0.1", "192.168.0.0/24"));
    System.out.println(IPUtils.isInRange("10.168.0.0", "10.168.0.0/32"));
  }
}
