package tech.iooo.boot.netty.utils;

import sun.misc.Unsafe;

public class UnsafeUtils {

  final static private Unsafe _unsafe;

  static {
    Unsafe tmpUnsafe = null;

    try {
      java.lang.reflect.Field field = Unsafe.class.getDeclaredField("theUnsafe");
      field.setAccessible(true);
      tmpUnsafe = (Unsafe) field.get(null);
    } catch (Exception e) {
      throw new Error(e);
    }

    _unsafe = tmpUnsafe;
  }

  public static final Unsafe unsafe() {
    return _unsafe;
  }
}
