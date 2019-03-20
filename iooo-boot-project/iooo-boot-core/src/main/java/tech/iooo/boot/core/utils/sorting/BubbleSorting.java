package tech.iooo.boot.core.utils.sorting;

/**
 * Created on 2019-03-19 09:43
 *
 * @author <a href="mailto:yangkizhang@gmail.com?subject=iooo-basic-knowledge-booster">Ivan97</a>
 */
public class BubbleSorting<T extends Comparable<T>> implements Sorting<T> {

  /*
   * 重复地走访过要排序的数列，一次比较两个元素，如果它们的顺序错误就把它们交换过来。
   * 走访数列的工作是重复地进行直到没有再需要交换
   *
   * 1. 初始状态：无序区为R[1..n]，有序区为空；
   * 2. 第i趟排序(i=0,1,2…n-1)开始时，当前无序区和有序区分别为R[0..n-i]和R[n-i+1..n]。
   *    对每一对相邻元素进行比较，从开始第一对到结尾的最后一对，如果第一个比第二个大，就交换它们两个，
   *    这样在最后的元素应该会是最大的数，使R[1..n-i-1]和R[n-i..n]分别变为记录个数减少1个的新无序
   *    区和记录个数增加1个的新有序区；
   * 3. 循环n-1次，直到排序完成。
   *
   * 冒泡排序在代码实现上是最简单的，不需要什么思考，两层for循环嵌套，比大小交换。
   *
   * 因为冒泡通常的例子都是让大的往后移，对于刚接触排序的人来说看来上面可能认为冒泡排序与选择排序是反向操作，
   * 其实冒泡排序也可以把小数向前移，这样能明显的看出冒泡排序和选择的排序的不同，针对无序区的元素，冒泡排序
   * 总是不断地交换，而选择排序是先找出最小的元素再做一次交换。
   *
   * 衍生算法，鸡尾酒排序，该排序从左往右找出最大值后，再从右往左，找出最小值，类似鸡尾酒搅拌左右循环,在某些
   * 情况下，优于冒泡排序。
   *
   * |     比较次数     | 赋值交换次数 | 时间复杂度 | 时间复杂度最好 | 时间复杂度最坏 | 空间复杂度 |       特点      | 衍生算法  |
   * |  N-1到N(N-1)/2  |    O(N^2)   |   O(N^2)  |    O(N)      |    O(N^2)    |    O(1)   | 简单稳定，效率低 | 鸡尾酒排序 |
   *
   * @param T[] arr
   * @return T[]
   */

  @Override
  public T[] apply(T[] arr) {
    int n = arr.length;
    for (int i = 0; i < n - 1; i++) {
      for (int j = 0; j < n - i - 1; j++) {
        if (arr[j].compareTo(arr[j + 1]) > 0) {
          // swap arr[j+1] and arr[i] 
          T temp = arr[j];
          arr[j] = arr[j + 1];
          arr[j + 1] = temp;
        }
      }
    }
    return arr;
  }
}
