package tech.iooo.boot.core.utils.sorting;

/**
 * Created on 2019-03-19 09:51
 *
 * @author <a href="mailto:yangkizhang@gmail.com?subject=iooo-basic-knowledge-booster">Ivan97</a>
 */
public class ShellSorting<T extends Comparable<T>> implements Sorting<T> {

  private static final Integer INTERVAL = 1;
  private static final Integer INTERVAL_TIMES = 3;

  /*
   * 插入排序的改进版，优先比较距离远的元素，减少交换次数
   * 
   * 1. 选择一个增量序列t1，t2，…，tk，l中ti>tj，tk=1；
   * 2. 按增量序列个数k，对序列进行k 趟排序；
   * 3. 每趟排序，根据对应的增量ti，将待排序列分割成若干长度为m 的子序列，分别对各子表进行直接插入排序。
   *    仅增量因子为1 时，整个序列作为一个表来处理，表长度即为整个序列的长度。
   *    
   * 希尔排序的时间复杂度与增量序列的选取有关，例如希尔增量时间复杂度为O(N²)，而Hibbard增量的希尔排序的时间复杂度为O(N1.5)，
   * 希尔排序时间复杂度的下界是Nlg2N。希尔排序没有快速排序算法快 O(N(lgN))，因此中等大小规模表现良好，对规模非常大的数据排
   * 序不是最优选择。但是比O(N²)复杂度的算法快得多。并且希尔排序非常容易实现，算法代码短而简单。此外，希尔算法在最坏的情况下
   * 和平均情况下执行效率相差不是很多，与此同时快速排序在最坏的情况下执行的效率会非常差。
   *
   * |     比较次数     |  赋值交换次数  |  时间复杂度  | 时间复杂度最好 | 时间复杂度最坏 | 空间复杂度 |      特点      | 衍生算法 |
   * |  N-1到N(N-1)/2  |    O(N^1.5)   |   O(N^1.5)  |    O(N)      |    O(N^2)    |    O(1)   |    简单中等    |   无     |
   * 
   * @param T[] arr
   * @return T[]
   */

  @Override
  public T[] apply(T[] arr) {

    int h = INTERVAL;
    int insertIndex;
    T insertElement;

    while (h < arr.length / INTERVAL_TIMES) {
      h = INTERVAL_TIMES * h + 1;
    }

    while (h >= 1) {
      for (int i = h; i < arr.length; i++) {
        insertIndex = i - h;
        insertElement = arr[i];

        while (insertIndex >= 0 && arr[insertIndex].compareTo(insertElement) > 0) {
          arr[insertIndex + h] = arr[insertIndex];
          insertIndex -= h;
        }
        arr[insertIndex + h] = insertElement;
      }
      h = h / INTERVAL_TIMES;
    }

    return arr;
  }
}
