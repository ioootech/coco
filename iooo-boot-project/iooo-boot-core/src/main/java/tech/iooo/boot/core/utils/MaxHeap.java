package tech.iooo.boot.core.utils;

/**
 * Created on 2019-04-26 17:28
 *
 * @author <a href="mailto:yangkizhang@gmail.com?subject=iooo-boot">Ivan97</a>
 */
public class MaxHeap<AnyType extends Comparable<? super AnyType>> {

  private static final int DEFAULT_CAPACITY = 10;
  private AnyType[] array;
  private int currentSize;

  public MaxHeap(AnyType[] arr) {
    currentSize = arr.length;
    array = (AnyType[]) new Comparable[currentSize + 1];
    int i = 1;
    for (int j = 0; j < arr.length; j++) {
      array[i++] = arr[j];
    }
    buildHeap();
  }

  public MaxHeap(int maxSize){
    array = (AnyType[]) new Comparable[maxSize];
    buildHeap();
  }

  public MaxHeap(){
    array = (AnyType[]) new Comparable[DEFAULT_CAPACITY];
    buildHeap();
  }

  /**
   * 方法名：buildHeap 说明：从中间开始到结束 不断上滤
   */
  private void buildHeap() {
    for (int i = currentSize / 2; i <= currentSize; i++) {
      percolateUp(i);
    }
  }

  private void percolateUp(int position) {
    AnyType temp = array[position];
    while (position > 1) {
      if (array[position / 2].compareTo(temp) < 0) {
        array[position] = array[position / 2];
        position /= 2;
      } else {
        break;
      }
    }
    array[position] = temp;
  }

  private boolean isEmpty() {
    return currentSize == 0;
  }

  public AnyType deleMax() {
    AnyType max = array[1];
    AnyType temp = array[currentSize--];
    int position = 1;
    int child;
    while (position * 2 <= currentSize) {
      child = 2 * position;
      if (child != currentSize && array[child].compareTo(array[child + 1]) < 0) {
        child++;
      }
      if (array[child].compareTo(temp) > 0) {
        array[position] = array[child];
      } else {
        break;
      }
      position = child;
    }
    array[position] = temp;
    return max;
  }

  public void insert(AnyType x) {
    if (currentSize == array.length - 1) {
      enlargeArray(2 * currentSize + 1);
    }
    array[++currentSize] = x;
    percolateUp(currentSize);
  }

  private void enlargeArray(int capacity) {
    AnyType[] oldArr = array;
    AnyType[] newArr = (AnyType[]) new Comparable[capacity];
    if (array.length - 1 >= 0) {
      System.arraycopy(oldArr, 1, newArr, 1, array.length - 1);
    }
    array = newArr;
  }

  @Override
  public String toString() {
    StringBuffer sb = new StringBuffer();
    for (int i = 1; i <= currentSize; i++) {
      sb.append(array[i] + " ");
    }
    return new String(sb);
  }

}
