package tech.iooo.boot.core.utils;

import org.junit.jupiter.api.Test;

/**
 * Created on 2018-12-27 15:13
 *
 * @author <a href="mailto:yangkizhang@gmail.com?subject=iooo-boot">Ivan97</a>
 */
class MaskUtilsTest {

  @Test
  void test() {
    System.out.println(MaskUtils.emailMasking("m11111@iooo.tech"));
    System.out.println(MaskUtils.phoneMasking("11"));
  }
}
