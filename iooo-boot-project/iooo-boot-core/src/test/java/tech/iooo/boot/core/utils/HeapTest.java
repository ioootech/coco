package tech.iooo.boot.core.utils;

import org.junit.jupiter.api.Test;

/**
 * Created on 2019-04-26 17:28
 *
 * @author <a href="mailto:yangkizhang@gmail.com?subject=iooo-boot">Ivan97</a>
 */
class HeapTest {

  @Test
  void test() {
    Integer[] arr = new Integer[]{9, 8, 7, 6, 5, 4, 3, 2};
    MinHeap min = new MinHeap(6);
    for (Integer integer : arr) {
      min.insert(integer);
    }
    min.insert(10);
    min.deleMin();
    System.out.println(min);
    Integer[] arr2 = new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
    MaxHeap max = new MaxHeap(arr2);
    max.deleMax();
    max.insert(10);
    System.out.println(max);
  }
}
