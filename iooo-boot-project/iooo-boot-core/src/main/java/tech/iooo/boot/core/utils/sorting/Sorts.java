package tech.iooo.boot.core.utils.sorting;

import lombok.experimental.UtilityClass;

/**
 * Created on 2019-03-20 13:17
 *
 * @author <a href="mailto:yangkizhang@gmail.com?subject=iooo-boot">Ivan97</a>
 */
@UtilityClass
public class Sorts {

  public <T extends Comparable<T>> InsertSorting<T> insertSorting(Class<T> type) {
    return new InsertSorting<>();
  }

  public <T extends Comparable<T>> SelectionSorting<T> selectionSorting(Class<T> type) {
    return new SelectionSorting<>();
  }

  public <T extends Comparable<T>> ShellSorting<T> shellSorting(Class<T> type) {
    return new ShellSorting<>();
  }

  public <T extends Comparable<T>> BubbleSorting<T> bubbleSorting(Class<T> type) {
    return new BubbleSorting<>();
  }

  public <T extends Comparable<T>> HeapSorting<T> heapSorting(Class<T> type) {
    return new HeapSorting<>();
  }

  public <T extends Comparable<T>> MergeSorting<T> mergeSorting(Class<T> type) {
    return new MergeSorting<>();
  }

  public <T extends Comparable<T>> QuickSorting<T> quickSorting(Class<T> type) {
    return new QuickSorting<>();
  }
}
