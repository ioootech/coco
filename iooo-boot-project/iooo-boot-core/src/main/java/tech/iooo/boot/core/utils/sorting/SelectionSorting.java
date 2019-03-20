package tech.iooo.boot.core.utils.sorting;

/**
 * Created on 2019-03-19 09:29
 *
 * @author <a href="mailto:yangkizhang@gmail.com?subject=iooo-basic-knowledge-booster">Ivan97</a>
 */
public class SelectionSorting<T extends Comparable<T>> implements Sorting<T> {

  /*
   * 在未排序序列中找到最小元素，放到排序序列的起始位置，然后，再从剩余未排序元素中继续寻找
   * 最小元素，然后放到已排序序列的末尾，循环直到所有元素均排序完毕。
   *
   * 1. 初始状态：无序区为R[1..n]，有序区为空；
   * 2. 第i趟排序(i=1,2,3…n-1)开始时，当前有序区和无序区分别为R[1..i-1]和R[i..n]。
   *    该趟排序从当前无序区中选出最小的记录 R[k]，将它与无序区的第1个记录R[i]交换，
   *    使R[1..i]和R[i+1..n]分别变为记录个数增加1个的新有序区和记录个数减少1个的新无序区；
   * 3. 循环n-1次，排序完成。
   *
   * 选择排序为两层for循环嵌套，内层循环始终去找最小值，放到最前面。交换次数比冒泡排序少很多，
   * 所以实际执行效率比冒泡排序快。
   *
   * 衍生算法，双向选择排序（每次循环，同时选出最大值放在末尾，最小值放在前方），可以提高选择效率。
   *
   * |     比较次数     | 赋值交换次数 | 时间复杂度 | 时间复杂度最好 | 时间复杂度最坏 | 空间复杂度 |        特点        |   衍生算法   |
   * |     N(N-1)/2    |     O(N)    |   O(N^2)  |    O(N^2)    |    O(N^2)    |    O(1)   |        简单        | 双向选择排序 |
   *
   * @param T[] arr
   * @return T[]
   */

  @Override
  public T[] apply(T[] arr) {

    int n = arr.length;

    // One by one move boundary of unsorted subarray 
    for (int i = 0; i < n - 1; i++) {
      // Find the minimum element in unsorted array 
      int minIdx = i;
      for (int j = i + 1; j < n; j++) {
        if (arr[j].compareTo(arr[minIdx]) < 0) {
          minIdx = j;
        }
      }

      // Swap the found minimum element with the first 
      // element 
      T temp = arr[minIdx];
      arr[minIdx] = arr[i];
      arr[i] = temp;
    }
    return arr;
  }
}
