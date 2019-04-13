package tech.iooo.boot.core.utils;

/**
 * Created on 2019-04-13 22:26
 *
 * @author <a href="mailto:yangkizhang@gmail.com?subject=iooo-boot">Ivan97</a>
 */
public class TwoBitMap {

  /**
   * 用两个bit为来标记某个元素的个数
   */
  private int bNum = 2;
  /**
   * 一个32位字节能标记多少个数
   */
  private int bSize = 32 / bNum;

  /**
   * 数据范围(0到2^32内的数)
   */
  private int numSize = Integer.MAX_VALUE;
  /**
   * 定义bitmap数组大小
   */
  private int arraySize;
  private int[] array;

  public TwoBitMap(int numSize) {
    Assert.isTrue(numSize >= 0, "numSize不能小于0");
    this.numSize = numSize;
    initBitMap();
  }

  public TwoBitMap() {
    initBitMap();
  }

  /**
   * 初始化BitMap
   */
  private void initBitMap() {
    arraySize = (int) Math.ceil((double) numSize / bSize);
    array = new int[arraySize];
    for (int i = 0; i < array.length; i++) {
      array[i] = 0;
    }
  }

  /**
   * 往BitMap中设置对应的数的个数
   *
   * @param x 要添加的数
   * @param num 对应的个数
   */
  public void set(int x, int num) {
    //获得bitMap的下标
    int m = x >> 4;
    //或 int m = x /bSize;

    //获得对应的位置
    int n = x % bSize;

    //将x对应位置上的数值先清零，但是有要保证其他位置上的数不变
    array[m] &= ~((0x3 << (2 * n)));
    //重新对x的个数赋值
    array[m] |= ((num & 3) << (2 * n));
//        printInfo(array[m]);
  }

  /**
   * 获取x在BitMap中的数量
   */
  public int get(int x) {
    int m = x >> 4;
    int n = x % bSize;
    return (array[m] & (0x3 << (2 * n))) >> (2 * n);
  }

  public String getInfo(int x) {
    return Integer.toBinaryString(get(x));
  }

  /**
   * 往BitMap中添加数 如果x的个数大于三，则不在添加(2个bit为最多只能表示到3:00 01 10 11)
   */
  public void add(int x) {
    int num = get(x);
    //只处理num小于3的
    if (num < 3) {
      set(x, num + 1);
    }
  }
}
