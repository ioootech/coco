package tech.iooo.boot.core.utils.sorting;

/**
 * Created on 2019-03-19 10:09
 *
 * @author <a href="mailto:yangkizhang@gmail.com?subject=iooo-basic-knowledge-booster">Ivan97</a>
 */
public class HeapSorting<T extends Comparable<T>> implements Sorting<T> {

  /*
   * 堆就是完全二叉树，分为最大堆和最小堆，最大堆要求节点的元素都要不小于其孩子（最小堆要求节点元素都不大于其孩子），
   * 对左右孩子的大小关系不做要求，所以处于最大堆的根节点的元素一定是这个堆中的最大值。堆排序算法就是抓住了堆的这一
   * 特点，每次都取堆顶的元素，将其放在序列最后面，然后将剩余的元素重新调整为最大堆，依此类推，最终得到排序的序列。
   *
   * 1. 将初始待排序关键字序列(R1,R2….Rn)构造成最大堆，此堆为初始的无序区；
   * 2. 将堆顶元素R[1]与最后一个元素R[n]交换，此时得到新的无序区(R1,R2,……Rn-1)和新的有序区(Rn),且满足R[1,2…n-1]<=R[n]；
   * 3. 由于交换后新的堆顶R[1]可能违反堆的性质，因此需要对当前无序区(R1,R2,……Rn-1)调整为新堆，然后再次将R[1]与无序区最后
   *    一个元素交换，得到新的无序区(R1,R2….Rn-2)和新的有序区(Rn-1,Rn)。不断重复此过程直到有序区的元素个数为n-1，则整个
   *    排序过程完成。
   *
   * 下标从0开始时，堆的父节点k的子节点为2k+1和2k+2，堆排序只需要NlgN的时间，而且算法稳定，只需要少量辅助空间，是最优的利用
   * 时间和空间的方法，但因为它的缓存命中率低，应用很少用它，多用于嵌入式。
   *
   * |     比较次数     | 赋值交换次数 | 时间复杂度 | 时间复杂度最好 | 时间复杂度最坏 | 空间复杂度 |          特点          |  衍生算法  |
   * |   小于2NlgN     |    O(NlgN)  |   O(NlgN) |   O(NlgN)    |   O(NlgN)    |    O(1)   |效率高占内存小缓存命中率低 | 多叉堆排序 |
   *
   * @param T[] arr
   * @return T[]
   */

  @Override
  public T[] apply(T[] arr) {

    // 将待排序的序列构建成一个大顶堆
    for (int i = arr.length / 2; i >= 0; i--) {
      heapAdjust(arr, i, arr.length);
    }

    // 逐步将每个最大值的根节点与末尾元素交换，并且再调整二叉树，使其成为大顶堆
    for (int i = arr.length - 1; i > 0; i--) {
      // 将堆顶记录和当前未经排序子序列的最后一个记录交换
      swap(arr, 0, i);
      // 交换之后，需要重新检查堆是否符合大顶堆，不符合则要调整
      heapAdjust(arr, 0, i);
    }
    return arr;
  }


  /**
   * 构建堆的过程
   *
   * @param arr 需要排序的数组
   * @param i 需要构建堆的根节点的序号
   * @param n 数组的长度
   */
  private void heapAdjust(T[] arr, int i, int n) {
    int child;
    T father;
    for (father = arr[i]; leftChild(i) < n; i = child) {
      child = leftChild(i);

      // 如果左子树小于右子树，则需要比较右子树和父节点
      if (child != n - 1 && arr[child].compareTo(arr[child + 1]) < 0) {
        child++; // 序号增1，指向右子树
      }

      // 如果父节点小于孩子结点，则需要交换
      if (father.compareTo(arr[child]) < 0) {
        arr[i] = arr[child];
      } else {
        break; // 大顶堆结构未被破坏，不需要调整
      }
    }
    arr[i] = father;
  }

  private int leftChild(int i) {
    return 2 * i + 1;
  }

  private void swap(T[] arr, int index1, int index2) {
    T tmp = arr[index1];
    arr[index1] = arr[index2];
    arr[index2] = tmp;
  }

}
