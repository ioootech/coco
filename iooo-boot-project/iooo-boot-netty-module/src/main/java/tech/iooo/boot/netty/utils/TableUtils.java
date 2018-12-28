package tech.iooo.boot.netty.utils;

public final class TableUtils {

  public static final int MAXIMUM_CAPACITY = 1 << 30;

  /**
   * 成倍扩容
   */
  public static final int tableSizeFor(final int cap) {
    int n = cap - 1;
    n |= n >>> 1;
    n |= n >>> 2;
    n |= n >>> 4;
    n |= n >>> 8;
    n |= n >>> 16;
    return (n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;
  }

  /**
   * 获取offset
   */
  public static final long offset(final long arrayBase, final int arrayShift, final int index) {
    return ((long) index << arrayShift) + arrayBase;
  }
}
