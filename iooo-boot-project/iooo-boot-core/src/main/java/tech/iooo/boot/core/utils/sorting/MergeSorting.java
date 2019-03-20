package tech.iooo.boot.core.utils.sorting;

import tech.iooo.boot.core.constants.SuppressTypeConstants;

/**
 * Created on 2019-03-19 10:28
 *
 * @author <a href="mailto:yangkizhang@gmail.com?subject=iooo-basic-knowledge-booster">Ivan97</a>
 */
public class MergeSorting<T extends Comparable<T>> implements Sorting<T> {

  private Object[] aux;

  /*
   * 递归的把已有序列均分为两个子序列，使子序列有序，合并子序列
   *
   * 1. 把长度为n的输入序列分成两个长度为n/2的子序列；
   * 2. 对这两个子序列分别采用归并排序；
   * 3. 将两个排序好的子序列合并成一个最终的排序序列。
   *
   * 归并排序是分治法的典型应用，高效稳定，但是归并排序需要一个数组长度的辅助空间，在空间成本高的时候不适合使用。
   *
   * |     比较次数     | 赋值交换次数 | 时间复杂度 | 时间复杂度最好 | 时间复杂度最坏 | 空间复杂度 |          特点     |  衍生算法   |
   * | NlgN到(2N-1)lgN |     O(N)    |   O(NlgN) |   O(NlgN)    |   O(NlgN)    |    O(N)   |  效率高稳定占内存  | 多路并归排序 |
   *
   * @param T[] arr
   * @return T[]
   */

  @Override
  public T[] apply(T[] arr) {
    aux = new Object[arr.length];

    //开始递归排序
    sort(arr, 0, arr.length - 1);

    return arr;
  }

  /**
   * 递归的排序归并
   */
  private void sort(T[] a, int left, int right) {
    //排序从左到右，确保右边比左边大
    if (right <= left) {
      return;
    }
    //找出中间位置
    int mid = (left + right) / 2;
    //将左半边排序
    sort(a, left, mid);
    //将右半边排序
    sort(a, mid + 1, right);
    //归并结果
    merge(a, left, mid, right);
  }

  /**
   * 原地归并方法
   */
  @SuppressWarnings(SuppressTypeConstants.ALL)
  private void merge(T[] a, int left, int mid, int right) {
    //左右半边起始位置
    int i = left, j = mid + 1;
    //将a[left..right]复制到aux[left..right]
    for (int k = left; k <= right; k++) {
      aux[k] = a[k];
    }
    //归并回到a[left..right]
    for (int k = left; k <= right; k++) {
      //i比mid大代表左半边用完，只有右半边有元素
      if (i > mid) {
        //右边元素给a[k]
        a[k] = (T) aux[j++];
        //j比right大代表右半边用完，只有左半边有元素
      } else if (j > right) {
        //左边元素给a[k]
        a[k] = (T) aux[i++];
        //如果右边元素大于左边
      } else if (((T) aux[j]).compareTo((T) aux[i]) < 0) {
        //右边元素给a[k]
        a[k] = (T) aux[j++];
        //否则左边大于等于右边
      } else {
        //左边元素给a[k]
        a[k] = (T) aux[i++];
      }
    }
  }
}
