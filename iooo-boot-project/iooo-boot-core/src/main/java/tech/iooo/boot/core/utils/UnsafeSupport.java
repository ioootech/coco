package tech.iooo.boot.core.utils;


import java.lang.reflect.Field;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.Unsafe;
import tech.iooo.boot.core.constants.SuppressTypeConstants;

/**
 * Created on 2019-04-11 15:27
 *
 * @author <a href="mailto:yangkizhang@gmail.com?subject=iooo-boot">Ivan97</a>
 */
@SuppressWarnings(SuppressTypeConstants.RESTRICTION)
public interface UnsafeSupport {
  
  Unsafe unsafe = InternalUnsafeBin.getUnsafe();
  
  boolean hasUnsafe = InternalUnsafeBin.hasUnsafe();

  enum InternalUnsafeBin {
    ;

    static final Logger logger = LoggerFactory.getLogger(UnsafeSupport.class);
    private static final Unsafe UNSAFE;

    static {
      String javaSpecVersion = System.getProperty("java.specification.version");
      if (logger.isDebugEnabled()) {
        logger.debug("Starting UnsafeSupport init in Java " + javaSpecVersion);
      }

      ByteBuffer direct = ByteBuffer.allocateDirect(1);
      Unsafe unsafe;

      // Get an Unsafe instance first, via the (still legit as of Java 9)
      // deep reflection trick on theUnsafe Field
      Object maybeUnsafe;
      try {
        final Field unsafeField = Unsafe.class.getDeclaredField("theUnsafe");
        unsafeField.setAccessible(true);
        // the unsafe instance
        maybeUnsafe = unsafeField.get(null);
      } catch (NoSuchFieldException | SecurityException | IllegalAccessException e) {
        maybeUnsafe = e;
      }

      // the conditional check here can not be replaced with checking that maybeUnsafe
      // is an instanceof Unsafe and reversing the if and else blocks; this is because an
      // instanceof check against Unsafe will trigger a class load and we might not have
      // the runtime permission accessClassInPackage.sun.misc
      if (maybeUnsafe instanceof Throwable) {
        unsafe = null;
        if (logger.isDebugEnabled()) {
          logger.debug("Unsafe unavailable - Could not get it via Field sun.misc.Unsafe.theUnsafe", (Throwable) maybeUnsafe);
        }
      } else {
        unsafe = (Unsafe) maybeUnsafe;
        if (logger.isTraceEnabled()) {
          logger.trace("sun.misc.Unsafe.theUnsafe ok");
        }
      }

      // ensure the unsafe supports all necessary methods to work around the mistake in the latest OpenJDK
      // https://github.com/netty/netty/issues/1061
      // https://www.mail-archive.com/jdk6-dev@openjdk.java.net/msg00698.html
      if (unsafe != null) {
        final Unsafe finalUnsafe = unsafe;
        Object maybeException;
        try {
          finalUnsafe.getClass().getDeclaredMethod(
              "copyMemory", Object.class, long.class, Object.class, long.class, long.class);
          maybeException = null;
        } catch (NoSuchMethodException | SecurityException e) {
          maybeException = e;
        }

        if (maybeException == null) {
          if (logger.isTraceEnabled()) {
            logger.trace("sun.misc.Unsafe.copyMemory ok");
          }
        } else {
          unsafe = null;
          if (logger.isDebugEnabled()) {
            logger.debug("Unsafe unavailable - failed on sun.misc.Unsafe.copyMemory", (Throwable) maybeException);
          }
        }
      }

      // finally check the Buffer#address
      if (unsafe != null) {
        final Unsafe finalUnsafe = unsafe;
        Object maybeAddressField;
        try {
          final Field field = Buffer.class.getDeclaredField("address");

          // Use Unsafe to read value of the address field.
          // This way it will not fail on JDK9+ which forbids changing the
          // access level via reflection.
          final long offset = finalUnsafe.objectFieldOffset(field);
          final long heapAddress = finalUnsafe.getLong(ByteBuffer.allocate(1), offset);
          final long directAddress = finalUnsafe.getLong(direct, offset);

          if (heapAddress != 0 && "1.8".equals(javaSpecVersion)) {
            maybeAddressField = new IllegalStateException("A heap buffer must have 0 address in Java 8, got " + heapAddress);
          } else if (heapAddress == 0 && !"1.8".equals(javaSpecVersion)) {
            maybeAddressField = new IllegalStateException("A heap buffer must have non-zero address in Java " + javaSpecVersion);
          } else if (directAddress == 0) {
            maybeAddressField = new IllegalStateException("A direct buffer must have non-zero address");
          } else {
            maybeAddressField = field;
          }

        } catch (NoSuchFieldException | SecurityException e) {
          maybeAddressField = e;
        }

        if (maybeAddressField instanceof Throwable) {
          if (logger.isDebugEnabled()) {
            logger.debug("Unsafe unavailable - failed on java.nio.Buffer.address", (Throwable) maybeAddressField);
          }

          // If we cannot access the address of a direct buffer, there's no point in using unsafe.
          // Let's just pretend unsafe is unavailable for overall simplicity.
          unsafe = null;
        } else {
          if (logger.isTraceEnabled()) {
            logger.trace("java.nio.Buffer.address ok");
          }
          if (logger.isDebugEnabled()) {
            logger.debug("Unsafe is available");
          }
        }
      }

      UNSAFE = unsafe;
    }

    public static Unsafe getUnsafe() {
      return UNSAFE;
    }

    public static boolean hasUnsafe() {
      return UNSAFE != null;
    }
  }
}
