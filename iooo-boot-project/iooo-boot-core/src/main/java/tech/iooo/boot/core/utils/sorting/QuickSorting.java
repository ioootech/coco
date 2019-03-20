package tech.iooo.boot.core.utils.sorting;

/**
 * Created on 2019-03-19 11:23
 *
 * @author <a href="mailto:yangkizhang@gmail.com?subject=iooo-basic-knowledge-booster">Ivan97</a>
 */
public class QuickSorting<T extends Comparable<T>> implements Sorting<T> {

  /*
   * 通过一趟排序将待排记录分隔成独立的两部分，其中一部分记录的关键字均比另一部分的关键字小，
   * 则可分别对这两部分记录继续进行排序，以达到整个序列有序。
   *
   * 1. 从数列中挑出一个元素，称为 “基准”（pivot）；
   * 2. 重新排序数列，所有元素比基准值小的摆放在基准前面，所有元素比基准值大的摆在基准的后面（相同的数可以到任一边）。
   *    在这个分区退出之后，该基准就处于数列的中间位置。这个称为分区（partition）操作；
   * 3. 递归地（recursive）把小于基准值元素的子数列和大于基准值元素的子数列排序。
   *
   * 快速排序是通常情况下的最优选择，高效只需要lgN级别的辅助空间，但是快速排序不稳定，受切分点的影响很大
   *
   * |     比较次数     | 赋值交换次数 | 时间复杂度 | 时间复杂度最好 | 时间复杂度最坏 | 空间复杂度 |      特点    |   衍生算法   |
   * |  NlgN到N(N-1)/2 |    O(NlgN)  |   O(NlgN) |   O(NlgN)    |   O(N^2)     |   O(lgN)  |  效率高不稳定 | 三向快速排序 |
   *
   * @param T[] arr
   * @return T[]
   */

  @Override
  public T[] apply(T[] arr) {
    qsort(arr, 0, arr.length - 1);
    return arr;
  }

  /**
   * 递归进行快速排序
   */
  private void qsort(T[] arr, int low, int high) {
    if (low < high) {
      //将数组分为两部分
      int pivot = partition(arr, low, high);
      //递归排序左子数组
      qsort(arr, low, pivot - 1);
      //递归排序右子数组
      qsort(arr, pivot + 1, high);
    }
  }


  /**
   * 快速排序切分
   */
  private int partition(T[] arr, int low, int high) {
    //枢轴记录
    T pivot = arr[low];
    while (low < high) {
      while (low < high && arr[high].compareTo(pivot) >= 0) {
        --high;
      }
      //交换比枢轴小的记录到左端
      arr[low] = arr[high];
      while (low < high && arr[low].compareTo(pivot) <= 0) {
        ++low;
      }
      //交换比枢轴小的记录到右端
      arr[high] = arr[low];
    }
    //扫描完成，枢轴到位
    arr[low] = pivot;
    //返回的是枢轴的位置
    return low;
  }
}
