package tech.iooo.boot.core.utils.aes;

import tech.iooo.boot.core.utils.Base64Utils;

/**
 * @author 龙也
 * @date 2019/11/21 10:36 上午
 */
public class TypeConvert {

  private static final String HEX_BASED_STR = "0123456789ABCDEF";

  /**
   * 字符串转换成十六进制字符串
   */
  public static String str2HexStr(String str) {
    char[] chars = HEX_BASED_STR.toCharArray();
    StringBuilder sb = new StringBuilder("");
    byte[] bs = str.getBytes();
    int bit;
    for (byte b : bs) {
      bit = (b & 0x0f0) >> 4;
      sb.append(chars[bit]);
      bit = b & 0x0f;
      sb.append(chars[bit]);
    }
    return sb.toString();
  }

  /**
   * Convert hex string to byte[]
   *
   * @param hexString the hex string
   * @return byte[]
   */
  public static byte[] hexStringToBytes(String hexString) {
    if (hexString == null || "".equals(hexString)) {
      return null;
    }
    hexString = hexString.toUpperCase();
    int length = hexString.length() / 2;
    char[] hexChars = hexString.toCharArray();
    byte[] d = new byte[length];
    for (int i = 0; i < length; i++) {
      int pos = i * 2;
      d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
    }
    return d;
  }

  /**
   * Convert char to byte
   *
   * @param c char
   * @return byte
   */
  private static byte charToByte(char c) {
    return (byte) HEX_BASED_STR.indexOf(c);
  }

  /**
   * 数组转换成十六进制字符串
   *
   * @param bArray byte[]
   * @return HexString
   */
  public static String bytesToHexString(byte[] bArray) {
    if (bArray == null || bArray.length == 0) {
      return null;
    }
    StringBuilder sb = new StringBuilder(bArray.length);
    String sTemp;
    for (byte b : bArray) {
      sTemp = Integer.toHexString(0xFF & b);
      if (sTemp.length() < 2) {
        sb.append(0);
      }
      sb.append(sTemp.toUpperCase());
    }
    return sb.toString();
  }

  /**
   * 十六进制字符串转换成字符串
   *
   * @return String
   */
  public static String hexStr2Str(String hexStr) {
    char[] hexChars = hexStr.toCharArray();
    byte[] bytes = new byte[hexStr.length() / 2];
    int n;
    for (int i = 0; i < bytes.length; i++) {
      n = HEX_BASED_STR.indexOf(hexChars[2 * i]) * 16;
      n += HEX_BASED_STR.indexOf(hexChars[2 * i + 1]);
      bytes[i] = (byte) (n & 0xff);
    }
    return new String(bytes);
  }

  /**
   * @param hexString String str = "000AB"
   */
  public static int hexString2Int(String hexString) {
    return Integer.valueOf(hexString, 16);
  }

  /**
   * 把byte转为字符串的bit
   */
  public static String byteToBitString(byte b) {
    return ""
        + (byte) ((b >> 7) & 0x1) + (byte) ((b >> 6) & 0x1)
        + (byte) ((b >> 5) & 0x1) + (byte) ((b >> 4) & 0x1)
        + (byte) ((b >> 3) & 0x1) + (byte) ((b >> 2) & 0x1)
        + (byte) ((b >> 1) & 0x1) + (byte) ((b) & 0x1);
  }

  /**
   * 把byte转为字符串数组的bit
   */
  public static String[] byteToBitStrings(byte b) {
    String[] bit = new String[8];
    bit[0] = "" + (byte) ((b >> 7) & 0x1);
    bit[1] = "" + (byte) ((b >> 6) & 0x1);
    bit[2] = "" + (byte) ((b >> 5) & 0x1);
    bit[3] = "" + (byte) ((b >> 4) & 0x1);
    bit[4] = "" + (byte) ((b >> 3) & 0x1);
    bit[5] = "" + (byte) ((b >> 2) & 0x1);
    bit[6] = "" + (byte) ((b >> 1) & 0x1);
    bit[7] = "" + (byte) ((b) & 0x1);
    return bit;
  }

  /**
   * base64字符串转byte[]
   *
   * @param base64Str base64字符串
   * @return byte[]
   */
  public static byte[] base64String2ByteFun(String base64Str) {
    return Base64Utils.decodeFromString(base64Str);
  }

  /**
   * byte[]转base64
   *
   * @param b byte[]
   * @return base64字符串
   */
  public static String byte2Base64StringFun(byte[] b) {
    return Base64Utils.encodeToString(b);
  }
}
