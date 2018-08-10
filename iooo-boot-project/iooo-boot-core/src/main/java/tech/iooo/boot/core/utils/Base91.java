package tech.iooo.boot.core.utils;

import java.nio.charset.StandardCharsets;
import java.util.BitSet;

/**
 * Created on 2018/8/2 下午5:27
 *
 * @author <a href="mailto:yangkizhang@gmail.com?subject=iooo-codewar-booster">Ivan97</a>
 */
public class Base91 {

  private static final int BASE = 91;
  private static final int PACK = 13;
  private static final int BITS_IN_BYTE = 8;
  private static final String TRANSLATION = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!#$%&()*+,./:;<=>?@[]^_`{|}~\"";

  public static String encode(String data) {
    byte[] bytes = data.getBytes(StandardCharsets.ISO_8859_1);

    BitSet bits = new BitSet();

    for (int i = 0; i < bytes.length; i++) {
      for (int j = 0; j < BITS_IN_BYTE; j++) {
        if ((bytes[i] & 1 << j) > 0) {
          bits.set(i * BITS_IN_BYTE + j);
        }
      }
    }

    StringBuilder result = new StringBuilder();
    BitSet packBits;
    for (int i = 0; i < bytes.length * BITS_IN_BYTE; i = i + PACK) {
      packBits = bits.get(i, i + PACK);
      int value = packBits.isEmpty() ? 0 : (int) packBits.toLongArray()[0];
      if (value > 0 && value <= 88) {
        packBits = bits.get(i, i + PACK + 1);
        value = (int) packBits.toLongArray()[0];
        i++;
      }
      result.append(TRANSLATION.charAt(value % BASE));
      if (value / BASE > 0) {
        result.append(TRANSLATION.charAt(value / BASE));
      }
    }
    return result.toString();
  }

  public static String decode(String data) {
    BitSet resultBits = new BitSet();
    for (int i = 0; i < data.length(); i = i + 2) {
      int value = TRANSLATION.indexOf(data.charAt(i));
      if (i + 1 < data.length()) {
        value += TRANSLATION.indexOf(data.charAt(i + 1)) * BASE;
      }
      BitSet bits = BitSet.valueOf(new long[]{value});
      for (int j = bits.nextSetBit(0); j >= 0; j = bits.nextSetBit(j + 1)) {
        resultBits.set(j + i / 2 * PACK);
      }
    }
    return new String(resultBits.toByteArray());
  }
}
