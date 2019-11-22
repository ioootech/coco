package tech.iooo.boot.core.utils;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import lombok.SneakyThrows;
import tech.iooo.boot.core.utils.aes.AESType;
import tech.iooo.boot.core.utils.aes.TypeConvert;

/**
 * @author 龙也
 * @date 2019/11/21 10:34 上午
 */
public class AesUtil {

  @SneakyThrows
  private static byte[] encryptOrDecrypt(int mode, byte[] byteContent, String key, byte[] iv, AESType type, String modeAndPadding) {
    KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
    //此处解决mac，linux报错
    SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
    random.setSeed(key.getBytes());
    keyGenerator.init(type.value, random);
    SecretKey secretKey = keyGenerator.generateKey();
    byte[] enCodeFormat = secretKey.getEncoded();
    SecretKeySpec keySpec = new SecretKeySpec(enCodeFormat, "AES");
    // 创建密码器
    Cipher cipher = Cipher.getInstance(modeAndPadding);
    if (null != iv) {
      //指定一个初始化向量 (Initialization vector，IV)， IV 必须是16位
      cipher.init(mode, keySpec, new IvParameterSpec(iv));
    } else {
      cipher.init(mode, keySpec);
    }
    return cipher.doFinal(byteContent);
  }

  @SneakyThrows
  public static byte[] encrypt(String source, String key, byte[] iv, AESType type, String encodeType) {
    return encryptOrDecrypt(Cipher.ENCRYPT_MODE, TypeConvert.hexStringToBytes(source), key, iv, type, encodeType);
  }

  @SneakyThrows
  public static byte[] encrypt(byte[] source, String key, byte[] iv, AESType type, String encodeType) {
    return encryptOrDecrypt(Cipher.ENCRYPT_MODE, source, key, iv, type, encodeType);
  }

  @SneakyThrows
  public static byte[] decrypt(String source, String key, byte[] iv, AESType type, String encodeType) {
    return encryptOrDecrypt(Cipher.DECRYPT_MODE, TypeConvert.hexStringToBytes(source), key, iv, type, encodeType);
  }

  @SneakyThrows
  public static byte[] decrypt(byte[] source, String key, byte[] iv, AESType type, String encodeType) {
    return encryptOrDecrypt(Cipher.DECRYPT_MODE, source, key, iv, type, encodeType);
  }

  @SneakyThrows
  public static String encryptHex(byte[] source, String key, byte[] iv, AESType type, String encodeType) {
    byte[] encodeByte = encryptOrDecrypt(Cipher.ENCRYPT_MODE, source, key, iv, type, encodeType);
    return TypeConvert.bytesToHexString(encodeByte);
  }

  @SneakyThrows
  public static String encryptHex(String source, String key, byte[] iv, AESType type, String encodeType) {
    byte[] encodeByte = encryptOrDecrypt(Cipher.ENCRYPT_MODE, TypeConvert.hexStringToBytes(source), key, iv, type, encodeType);
    return TypeConvert.bytesToHexString(encodeByte);
  }

  @SneakyThrows
  public static String decryptHex(byte[] source, String key, byte[] iv, AESType type, String encodeType) {
    byte[] decodeByte = encryptOrDecrypt(Cipher.DECRYPT_MODE, source, key, iv, type, encodeType);
    return new String(decodeByte, StandardCharsets.UTF_8);
  }

  @SneakyThrows
  public static String decryptHex(String source, String key, byte[] iv, AESType type, String encodeType) {
    byte[] decodeByte = encryptOrDecrypt(Cipher.DECRYPT_MODE, TypeConvert.hexStringToBytes(source), key, iv, type, encodeType);
    return new String(decodeByte, StandardCharsets.UTF_8);
  }
}