package tech.iooo.boot;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.crypto.Cipher;
import org.apache.commons.codec.binary.Base64;

/**
 * @author 龙也
 * @date 2019-07-10
 */
public class RsaUtils {

  //加密算法
  private static final String SHA_256_WITH_RSA = "SHA256WithRSA";
  private static final String RSA = "RSA";

  //编码
  private static final Charset CHARSET_UTF_8 = StandardCharsets.UTF_8;

  private static final String RSA_PRIVATE_PREFIX = "BEGIN PRIVATE KEY";
  private static final String RSA_PRIVATE_SUFFIX = "END PRIVATE KEY";
  private static final String RSA_PUBLIC_PREFIX = "BEGIN PUBLIC KEY";
  private static final String RSA_PUBLIC_SUFFIX = "END PUBLIC KEY";

  private static final List<String> FLAGS = Arrays.asList(RSA_PRIVATE_PREFIX, RSA_PRIVATE_SUFFIX, RSA_PUBLIC_PREFIX, RSA_PUBLIC_SUFFIX);

  private static final Pattern p = Pattern.compile("\\s*|\t|\r|\n");

  /**
   * 生成公私钥对
   *
   * @return String[0]:公钥，String[1]：私钥
   */
  public static String[] generateKeyPair() {
    KeyPairGenerator keyPairGenerator = null;
    try {
      keyPairGenerator = KeyPairGenerator.getInstance(RSA);
      keyPairGenerator.initialize(1024, new SecureRandom());
      KeyPair keyPair = keyPairGenerator.generateKeyPair();
      RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
      RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
      return new String[]{new Base64().encodeToString(publicKey.getEncoded()),
          new Base64().encodeToString(privateKey.getEncoded())};
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * 使用私钥给入参签名
   *
   * @param privateKey 私钥
   * @param param 签名的数据
   * @return 返回入参签名16进制字符串
   */
  public static String sign(String privateKey, String param) {
    try {
      //获取privatekey
      byte[] privateKeyByte = new Base64().decode(resolve(privateKey));
      KeyFactory keyfactory = KeyFactory.getInstance(RSA);
      PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(privateKeyByte);
      PrivateKey key = keyfactory.generatePrivate(pkcs8EncodedKeySpec);

      //用私钥给入参加签
      Signature sign = Signature.getInstance(SHA_256_WITH_RSA);
      sign.initSign(key);
      sign.update(param.getBytes());

      byte[] signature = sign.sign();
      //将签名的入参转换成16进制字符串
      return bytesToHexStr(signature);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * 用公钥验证签名
   *
   * @param param 入参
   * @param signature 使用私钥签名的入参字符串
   * @param publicKey 公钥
   * @return 返回验证结果
   */

  public static boolean verifySign(String param, String signature, String publicKey) {
    try {
      //获取公钥
      KeyFactory keyFactory = KeyFactory.getInstance(RSA);
      byte[] publicKeyByte = new Base64().decode(resolve(publicKey));
      X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(publicKeyByte);
      PublicKey key = keyFactory.generatePublic(x509EncodedKeySpec);

      //用获取到的公钥对   入参中未加签参数param 与  入参中的加签之后的参数signature 进行验签
      Signature sign = Signature.getInstance(SHA_256_WITH_RSA);
      sign.initVerify(key);
      sign.update(param.getBytes());

      //将16进制码转成字符数组
      byte[] hexByte = hexStrToBytes(signature);
      //验证签名
      return sign.verify(hexByte);

    } catch (Exception e) {
      e.printStackTrace();
    }
    return false;
  }

  /**
   * 使用公钥加密
   *
   * @param publicKey 公钥
   * @param param 私钥
   * @return 加密后的字符串
   */
  public static String RSAPublicEncrypt(String publicKey, String param) throws Exception {

    try {
      byte[] publicKeyByte = new Base64().decode(resolve(publicKey));
      KeyFactory keyfactory = KeyFactory.getInstance(RSA);
      X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(publicKeyByte);
      RSAPublicKey key = (RSAPublicKey) keyfactory.generatePublic(x509EncodedKeySpec);
      // 使用默认RSA
      Cipher cipher = Cipher.getInstance(RSA);
      cipher.init(Cipher.ENCRYPT_MODE, key);
      byte[] output = cipher.doFinal(param.getBytes(CHARSET_UTF_8));
      return bytesToHexStr(output);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * 使用私钥解密
   *
   * @param privateKey 私钥
   * @param encryptParam 公钥加密的字符串
   * @return 解密后的字符串
   */
  public static String RSAPrivateDecrypt(String privateKey, String encryptParam) throws Exception {

    Cipher cipher = null;
    try {
      // 使用默认RSA
      byte[] privateKeyByte = new Base64().decode(resolve(privateKey));
      KeyFactory keyfactory = KeyFactory.getInstance(RSA);
      PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(privateKeyByte);
      RSAPrivateKey key = (RSAPrivateKey) keyfactory.generatePrivate(pkcs8EncodedKeySpec);

      cipher = Cipher.getInstance(RSA);
      cipher.init(Cipher.DECRYPT_MODE, key);
      byte[] output = cipher.doFinal(hexStrToBytes(encryptParam));
      return new String(output);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * 私钥加密，公钥解密
   *
   * @param privateKey 私钥
   * @param param 需要加密的数据
   * @return 返回加密后的数据
   */
  public static String RSAPrivateEncrypt(String privateKey, String param) {
    try {

      // 使用默认RSA
      byte[] privateKeyByte = new Base64().decode(resolve(privateKey));
      KeyFactory keyfactory = KeyFactory.getInstance(RSA);
      PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(privateKeyByte);
      RSAPrivateKey key = (RSAPrivateKey) keyfactory.generatePrivate(pkcs8EncodedKeySpec);
      Cipher cipher = Cipher.getInstance(RSA);
      cipher.init(Cipher.ENCRYPT_MODE, key);
      byte[] output = cipher.doFinal(param.getBytes(CHARSET_UTF_8));
      return bytesToHexStr(output);

    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * 私钥加密，公钥解密
   *
   * @param publicKey 公钥
   * @param encryptParam 需要解密的数据
   * @return 返回解密后的数据
   */
  public static String RSAPublicDecrypt(String publicKey, String encryptParam) {
    try {
      byte[] publicKeyByte = new Base64().decode(resolve(publicKey));
      KeyFactory keyfactory = KeyFactory.getInstance(RSA);
      X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(publicKeyByte);
      RSAPublicKey key = (RSAPublicKey) keyfactory.generatePublic(x509EncodedKeySpec);
      // 使用默认RSA
      Cipher cipher = Cipher.getInstance(RSA);
      cipher.init(Cipher.DECRYPT_MODE, key);
      byte[] output = cipher.doFinal(hexStrToBytes(encryptParam));
      return new String(output);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }


  /**
   * byte数组转换成十六进制字符串
   *
   * @param bytes byte数组
   * @return 返回十六进制字符串
   */
  private static String bytesToHexStr(byte[] bytes) {
    StringBuilder stringBuffer = new StringBuilder();
    for (byte aByte : bytes) {
      stringBuffer.append(Integer.toHexString(0x0100 + (aByte & 0x00FF)).substring(1).toUpperCase());
    }
    return stringBuffer.toString();
  }

  /**
   * 十六进制字符串转成byte数组
   *
   * @param hexStr 十六进制字符串
   * @return 返回byte数组
   */
  private static byte[] hexStrToBytes(String hexStr) {
    byte[] bytes = new byte[hexStr.length() / 2];
    for (int i = 0; i < bytes.length; i++) {
      bytes[i] = (byte) Integer.parseInt(hexStr.substring(2 * i, 2 * i + 2), 16);
    }
    return bytes;
  }

  private static String resolve(String text) {
    return replaceBlank(text.replace(RSA_PRIVATE_PREFIX, "")
        .replace(RSA_PRIVATE_SUFFIX, "")
        .replace(RSA_PUBLIC_PREFIX, "")
        .replace(RSA_PUBLIC_SUFFIX, "")
        .replaceAll("-", ""));
  }

  private static String replaceBlank(String str) {
    String dest = "";
    if (str != null) {
      Matcher m = p.matcher(str);
      dest = m.replaceAll("");
    }
    return dest;
  }


  public static void main(String[] args) throws Exception {

    String[] keyPair = new String[]{"", ""};
    keyPair[0] = "-----BEGIN PUBLIC KEY-----\n"
        + "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDutRAuw3HdjqxGaIhw5eL95JOn\n"
        + "XBAFsF/R6onZNxnLzzmLPw9EUwWUWjKHXa0nAeiWsBUbGLCQ2R/hVfjk9h1jM+aY\n"
        + "YVuh86c0c3LpvV5eQqXTDxtsaTt//HLSgPCS1PK5y80ryGYV7a+fJZSJZknU7kPR\n"
        + "d6o3E/kN4W4ujEX3wwIDAQAB\n"
        + "-----END PUBLIC KEY-----\n";
    keyPair[1] = "-----BEGIN PRIVATE KEY-----\n"
        + "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAO61EC7Dcd2OrEZo\n"
        + "iHDl4v3kk6dcEAWwX9Hqidk3GcvPOYs/D0RTBZRaModdrScB6JawFRsYsJDZH+FV\n"
        + "+OT2HWMz5phhW6HzpzRzcum9Xl5CpdMPG2xpO3/8ctKA8JLU8rnLzSvIZhXtr58l\n"
        + "lIlmSdTuQ9F3qjcT+Q3hbi6MRffDAgMBAAECgYEAtqqaCp8HeHsge/SsTHCu5olP\n"
        + "MGfz3tacTL/E4xMWS291YohL/4XkpCTtu8bEpTABs6ZlUEnxe7abfc1TXKq1yuTb\n"
        + "FKf+bdR+oaEXIbxcs8UhveW9wQfgSgvOrXLd4Y2vhGMyRxwe6udPDS1iSs+cYSxn\n"
        + "/sIO45fuGWeZ1AXJ/XECQQD7pyKh2D+VvNjBgf13RkJwM/lCm8ENdN+OadHPAt/o\n"
        + "ySlPxIdTZcUMOmUisXbupBVSFi78GGJ4pBiYBkoQ8MjLAkEA8tSt/XJbM04dzvt4\n"
        + "S43IsylGDeu8DWie/WYZS92jQ4wg4RyAtnO6/61if6VPjz1oIkepgZ7Rfm+29Mkz\n"
        + "/3HF6QJBANEPp5DG4wQVkAkqYZSuNNGcC+6PEJGKT5KzTI7qt3NfcseG4FmjSVEA\n"
        + "TjbFMaXNgLziGwRpiZzpzli1mOB7+IMCQQCldTMEgZoQ0EZRVVl3Kam6IgJg0+Iz\n"
        + "xLiocpwD7IYlLWLt4Az3xM2DDzIRRaN7N10wFmIyNvQJ0bb4/euXP3+ZAkAg1Adx\n"
        + "sqp5YbwNy2nQamdxC6dgqvQRpHOJ05DzyFD7qi5mbacjxYoSfEjG+ClkUmo839yA\n"
        + "Oc430DdgtorISnKe\n"
        + "-----END PRIVATE KEY-----\n";

    System.out.println("公钥>>" + keyPair[0]);
    System.out.println("私钥>>" + keyPair[1]);

    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("name", "yanshao");
    jsonObject.addProperty("ab", "ab");
    jsonObject.addProperty("age", 25);
    jsonObject.addProperty("address", "武汉");
    Gson gson = new Gson();
    String params = gson.toJson(jsonObject);

    System.out.println("入参>>>" + params);
    String sign = sign(keyPair[1], params);
    System.out.println("签名后的参数>>>" + sign);
    System.out.println("验证结果>>>" + verifySign(params, sign, keyPair[0]));

    String encrypt = RSAPublicEncrypt(keyPair[0], params);
    System.out.println("公钥加密>>>" + encrypt);
    System.out.println("私钥解密>>>" + RSAPrivateDecrypt(keyPair[1], encrypt));

    String encrypt1 = RSAPrivateEncrypt(keyPair[1], params);
    System.out.println("私钥加密>>>" + encrypt1);
    System.out.println("公钥解密>>>" + RSAPublicDecrypt(keyPair[0], encrypt1));
  }
}
