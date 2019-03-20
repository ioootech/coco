package tech.iooo.boot.core.utils.sorting;

/**
 * Created on 2019-03-18 17:43
 *
 * @author <a href="mailto:yangkizhang@gmail.com?subject=iooo-basic-knowledge-booster">Ivan97</a>
 */
public class InsertSorting<T extends Comparable<T>> implements Sorting<T> {

  /*
   * 对于未排序数据，在已排序序列中从后向前扫描，找到相应位置并插入
   *
   * 1. 从第一个元素开始，该元素认为已经被排序；
   * 2. 取出下一个元素，在已排序的元素序列中从后向前扫描；
   * 3. 如果已排序元素大于新元素，新元素继续比较前一个元素，直到找到已排序的元素小于或者等于新元素的位置；
   * 4. 将新元素插入到该位置后；
   * 5. 重复步骤2~4。
   *
   * 插入排序为两层循环嵌套，时间复杂度O(N2)
   *
   * 插入排序的while循环是先比较，移动待插入的位置，循环结束才真正交换数据位置。
   *
   * 这里需要注意，常用的for循环嵌套进行插入排序会每次比较都和前面元素交换直到插入到待插入位置，上面的内循环用while寻找
   * 待插入位置，把其他元素后移的算法更合理，每次插入只一次进行一次交换。因插入排序每次只比较相邻一位数据，对于逆序较多的
   * 数组效率低，衍生算法希尔排序，会大幅加快逆序交换
   *
   * |     比较次数     | 赋值交换次数 | 时间复杂度 | 时间复杂度最好 | 时间复杂度最坏 | 空间复杂度 |        特点        | 衍生算法 |
   * |  N-1到N(N-1)/2  |    O(N^2)   |   O(N^2)  |    O(N)      |    O(N^2)    |    O(1)   |简单稳定，适合有序初始| 希尔排序 |
   *
   * @param T[] arr
   * @return T[]
   */

  @Override
  public T[] apply(T[] array) {

    int length = array.length;
    if (length > 0) {
      for (int i = 1; i < length; i++) {
        T temp = array[i];
        int j = i;

        for (; j > 0 && array[j - 1].compareTo(temp) > 0; j--) {
          array[j] = array[j - 1];
        }
        array[j] = temp;
      }
    }
    return array;
  }
}
