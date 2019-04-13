package tech.iooo.boot.core.utils;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * Created on 2019-04-13 22:33
 *
 * @author <a href="mailto:yangkizhang@gmail.com?subject=iooo-boot">Ivan97</a>
 */
class TwoBitMapTest {

  @Test
  void test() {
    List<Integer> list = Arrays.asList(1, 5, 3, 5, 233, 2, 5, 1, 2, 34, 4, 3, 45, 5, 2, 4, 3, 2, 2, 4, 6, 9, 6, 9, 45);
    TwoBitMap twoBitMap = new TwoBitMap();
    list.forEach(twoBitMap::add);
    System.out.println(twoBitMap.get(45));
    System.out.println(twoBitMap.getInfo(45));
  }
}
