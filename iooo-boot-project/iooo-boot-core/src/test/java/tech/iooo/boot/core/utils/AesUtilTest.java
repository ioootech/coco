package tech.iooo.boot.core.utils;

import static tech.iooo.boot.core.utils.AesUtil.decrypt;
import static tech.iooo.boot.core.utils.AesUtil.decryptHex;
import static tech.iooo.boot.core.utils.AesUtil.encrypt;
import static tech.iooo.boot.core.utils.AesUtil.encryptHex;

import java.nio.charset.StandardCharsets;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import tech.iooo.boot.core.utils.aes.AESType;
import tech.iooo.boot.core.utils.aes.EncodeType;
import tech.iooo.boot.core.utils.aes.TypeConvert;

/**
 * @author 龙也
 * @date 2019/11/21 10:40 上午
 */
@Slf4j
class AesUtilTest {

  @Test
  @SneakyThrows
  void test1() {
    /**
     * 1.4 AES/OFB
     * AES/OFB/NoPadding
     * AES/OFB/PKCS5Padding
     * AES/OFB/ISO10126Padding
     */
    byte[] encrypt;
    byte[] decrypt;
    System.out.println("【1.4】AES_OFB模式");
    String content = "在线助手";
    // 生成密钥需要的密码值
    String key = "http://www.it399.com";
    encrypt = encrypt(content.getBytes(StandardCharsets.UTF_8), key, getIv(), AESType.AES_128, EncodeType.AES_OFB_PKCS5Padding);
    decrypt = decrypt(encrypt, key, getIv(), AESType.AES_128, EncodeType.AES_OFB_PKCS5Padding);
    log(encrypt, decrypt);
    encrypt = encrypt(content.getBytes(StandardCharsets.UTF_8), key, getIv(), AESType.AES_192, EncodeType.AES_OFB_PKCS5Padding);
    decrypt = decrypt(encrypt, key, getIv(), AESType.AES_192, EncodeType.AES_OFB_PKCS5Padding);
    log(encrypt, decrypt);
    encrypt = encrypt(content.getBytes(StandardCharsets.UTF_8), key, getIv(), AESType.AES_256, EncodeType.AES_OFB_PKCS5Padding);
    decrypt = decrypt(encrypt, key, getIv(), AESType.AES_256, EncodeType.AES_OFB_PKCS5Padding);
    log(encrypt, decrypt);
  }

  @Test
  @SneakyThrows
  void test2() {
    String encrypt;
    String decrypt;
    System.out.println("【1】AES不指定模式和填充，默认为 ECB/PKCS5Padding，输入可以不是16字节，也不需要填充向量\n");
    // 需要加密的内容
    String content = "在线助手";
    // 生成密钥需要的密码值
    String key = "www.it399.com111";
    System.out.println("content： " + content + "\nkey： " + key);
    //默认方式  每次加密都不一样，但是秘钥是一样的，所以解密还是一样的
    // 内容加密后的值
    encrypt = encryptHex(content, key, null, AESType.AES_128, EncodeType.AES_DEFAULT);
    // 被加密的内容解密后的值
    decrypt = decryptHex(encrypt, key, null, AESType.AES_128, EncodeType.AES_DEFAULT);
    log(encrypt, decrypt);
    System.out.println("【2】AES_CBC_NoPadding模式，输入必须是16*n字节，需要填充向量\n");
    // 内容加密后的值
    //待加密内容不足16*n位 报错javax.crypto.IllegalBlockSizeException: Input length not multiple of 16 bytes
    //需要填充向量，不然报错java.security.InvalidKeyException: Parameters missing
    //得到加密后的内容先base64编码再解码再传给解码，不然直接转回乱码
    content = "哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈";
    encrypt = encryptHex(content, key, null, AESType.AES_256, EncodeType.AES_CBC_NoPadding);
    decrypt = decryptHex(encrypt, key, null, AESType.AES_256, EncodeType.AES_CBC_NoPadding);
    log(encrypt, decrypt);
  }


  @Test
  @SneakyThrows
  void test() {

    String content = "在线助手";
    // 生成密钥需要的密码值
    String key = "www.it399.com";
    byte[] encrypt;
    /**
     * AES加密方式一:AES不指定模式和填充，默认为 ECB/PKCS5Padding
     */
    //        System.out.println("【0】AES不指定模式和填充，默认为 ECB/PKCS5Padding，输入可以不是16字节，也不需要填充向量\n");
    //        //128
    //        encrypt = encryptOrdecrypt(true,content.getBytes(CHARSET),key,null,AESType.AES_128,EncodeType.AES_DEFAULT);
    //        encryptOrdecrypt(false,encrypt,key,null,AESType.AES_128,EncodeType.AES_DEFAULT);
    //        //192
    //        encrypt = encryptOrdecrypt(true,content.getBytes(CHARSET),key,getIV(),AESType.AES_192,EncodeType.AES_DEFAULT);
    //        encryptOrdecrypt(false,encrypt,key,getIV(),AESType.AES_192,EncodeType.AES_DEFAULT);
    //        //256
    //        encrypt = encryptOrdecrypt(true,content.getBytes(CHARSET),key,null,AESType.AES_256,EncodeType.AES_DEFAULT);
    //        encryptOrdecrypt(false,encrypt,key,null,AESType.AES_256,EncodeType.AES_DEFAULT);
    //        /**
    //         * 1.1 AES/CBC （需要填充向量16*n）
    //         * AES/CBC/NoPadding
    //         * AES/CBC/PKCS5Padding
    //         * AES/CBC/ISO10126Padding
    //         */
    //        System.out.println("【1.1】AES_CBC_NoPadding模式，需要填充向量,待加密必须是16*n");
    //        content = "在线助手在线助手在线助手在线助手";
    //        key = "www.it399.com";
    //        encrypt = encryptOrdecrypt(true,content.getBytes(CHARSET),key,getIV(),AESType.AES_128,EncodeType.AES_CBC_NoPadding);
    //        encryptOrdecrypt(false,encrypt,key,getIV(),AESType.AES_128,EncodeType.AES_CBC_NoPadding);
    //        encrypt = encryptOrdecrypt(true,content.getBytes(CHARSET),key,getIV(),AESType.AES_192,EncodeType.AES_CBC_PKCS5Padding);
    //        encryptOrdecrypt(false,encrypt,key,getIV(),AESType.AES_192,EncodeType.AES_CBC_PKCS5Padding);
    //        encrypt = encryptOrdecrypt(true,content.getBytes(CHARSET),key,getIV(),AESType.AES_256,EncodeType.AES_CBC_ISO10126Padding);
    //        encryptOrdecrypt(false,encrypt,key,getIV(),AESType.AES_256,EncodeType.AES_CBC_ISO10126Padding);
    //        /**
    //         * 1.2 AES/CFB
    //         * AES/CBC/NoPadding
    //         * AES/CBC/PKCS5Padding
    //         * AES/CBC/ISO10126Padding
    //         */
    //        System.out.println("【1.2】AES_CFB_NoPadding模式\n");
    //        content = "在线助手";
    //        // 生成密钥需要的密码值
    //        key = "http://www.it399.com";
    //        encrypt = encryptOrdecrypt(true,content.getBytes(CHARSET),key,getIV(),AESType.AES_128,EncodeType.AES_CFB_PKCS5Padding);
    //        encryptOrdecrypt(false,encrypt,key,getIV(),AESType.AES_128,EncodeType.AES_CFB_PKCS5Padding);
    //        encrypt = encryptOrdecrypt(true,content.getBytes(CHARSET),key,getIV(),AESType.AES_192,EncodeType.AES_CFB_PKCS5Padding);
    //        encryptOrdecrypt(false,encrypt,key,getIV(),AESType.AES_192,EncodeType.AES_CFB_PKCS5Padding);
    //        encrypt = encryptOrdecrypt(true,content.getBytes(CHARSET),key,getIV(),AESType.AES_256,EncodeType.AES_CFB_PKCS5Padding);
    //        encryptOrdecrypt(false,encrypt,key,getIV(),AESType.AES_256,EncodeType.AES_CFB_PKCS5Padding);
    //        /**
    //         * 1.2 AES/ECB
    //         * AES/ECB/NoPadding
    //         * AES/ECB/PKCS5Padding
    //         * AES/ECB/ISO10126Padding
    //         */
    //        System.out.println("【1.3】AES_ECB模式");
    //        content = "在线助手";
    //        // 生成密钥需要的密码值
    //        key = "http://www.it399.com";
    //        encrypt = encryptOrdecrypt(true,content.getBytes(CHARSET),key,null,AESType.AES_128,EncodeType.AES_ECB_PKCS5Padding);
    //        encryptOrdecrypt(false,encrypt,key,null,AESType.AES_128,EncodeType.AES_ECB_PKCS5Padding);
    //        encrypt = encryptOrdecrypt(true,content.getBytes(CHARSET),key,null,AESType.AES_192,EncodeType.AES_ECB_PKCS5Padding);
    //        encryptOrdecrypt(false,encrypt,key,null,AESType.AES_192,EncodeType.AES_ECB_PKCS5Padding);
    //        encrypt = encryptOrdecrypt(true,content.getBytes(CHARSET),key,null,AESType.AES_256,EncodeType.AES_ECB_PKCS5Padding);
    //        encryptOrdecrypt(false,encrypt,key,null,AESType.AES_256,EncodeType.AES_ECB_PKCS5Padding);

    /**
     * 1.5 AES/PCBC
     * AES/PCBC/NoPadding
     * AES/PCBC/PKCS5Padding
     * AES/PCBC/ISO10126Padding
     */
    System.out.println("【1.5】AES_PCBC模式");
    content = "在线助手";
    // 生成密钥需要的密码值
    key = "http://www.it399.com";
    encrypt = encrypt(content.getBytes(StandardCharsets.UTF_8), key, getIv(), AESType.AES_128, EncodeType.AES_PCBC_PKCS5Padding);
    decrypt(encrypt, key, getIv(), AESType.AES_128, EncodeType.AES_PCBC_PKCS5Padding);
    encrypt = encrypt(content.getBytes(StandardCharsets.UTF_8), key, getIv(), AESType.AES_192, EncodeType.AES_PCBC_PKCS5Padding);
    decrypt(encrypt, key, getIv(), AESType.AES_192, EncodeType.AES_PCBC_PKCS5Padding);
    encrypt = encrypt(content.getBytes(StandardCharsets.UTF_8), key, getIv(), AESType.AES_256, EncodeType.AES_PCBC_PKCS5Padding);
    decrypt(encrypt, key, getIv(), AESType.AES_256, EncodeType.AES_PCBC_PKCS5Padding);
    //
    //        /**1.3 AES/CBC
    //         * AES_CBC_NoPadding模式（填充向量可选）
    //         */
    System.out.println("【1.3】AES_CBC_NoPadding模式");
    content = "在线助手在线助手在线助手在线助手";
    // 生成密钥需要的密码值
    key = "www.it399.com";
    encrypt = encrypt(content.getBytes(StandardCharsets.UTF_8), key, getIv(), AESType.AES_128, EncodeType.AES_CBC_NoPadding);
    decrypt(encrypt, key, getIv(), AESType.AES_128, EncodeType.AES_CBC_NoPadding);
    content = "在线助手";
    // 生成密钥需要的密码值
    key = "www.it399.com";
    encrypt = encrypt(content.getBytes(StandardCharsets.UTF_8), key, getIv(), AESType.AES_192, EncodeType.AES_CBC_PKCS5Padding);
    decrypt(encrypt, key, getIv(), AESType.AES_192, EncodeType.AES_CBC_PKCS5Padding);
    encrypt = encrypt(content.getBytes(StandardCharsets.UTF_8), key, getIv(), AESType.AES_256, EncodeType.AES_CBC_ISO10126Padding);
    decrypt(encrypt, key, getIv(), AESType.AES_256, EncodeType.AES_CBC_ISO10126Padding);
    //
    //
    //
    //        /**
    //         * 2.1 AES/CFB 128/192/256位加解密
    //         * AES_CFB_NoPadding模式（填充向量可选）
    //         */
    //        System.out.println("【2.1】AES_CFB_NoPadding模式，需要填充向量\n");
    //        content = "在线助手";
    //        // 生成密钥需要的密码值
    //        key = "www.it399.com";
    //        encrypt = encryptOrdecrypt(true,content.getBytes(CHARSET),key,null,AESType.AES_128,EncodeType.AES_CFB_NoPadding);
    //        encryptOrdecrypt(false,encrypt,key,null,AESType.AES_128,EncodeType.AES_CFB_NoPadding);
    //        encrypt = encryptOrdecrypt(true,content.getBytes(CHARSET),key,null,AESType.AES_192,EncodeType.AES_CFB_NoPadding);
    //        encryptOrdecrypt(false,encrypt,key,null,AESType.AES_192,EncodeType.AES_CFB_NoPadding);
    //        encrypt = encryptOrdecrypt(true,content.getBytes(CHARSET),key,null,AESType.AES_256,EncodeType.AES_CFB_NoPadding);
    //        encryptOrdecrypt(false,encrypt,key,null,AESType.AES_256,EncodeType.AES_CFB_NoPadding);
    //
    //        /**
    //         * 2.2 AES/CFB
    //         * AES_CFB_NoPadding模式（填充向量可选）
    //         */
    //        System.out.println("【2.2】AES_CFB_NoPadding模式\n");
    //        content = "在线助手";
    //        // 生成密钥需要的密码值
    //        key = "www.it399.com";
    //        encrypt = encryptOrdecrypt(true,content.getBytes(CHARSET),key,null,AESType.AES_128,EncodeType.AES_CFB_PKCS5Padding);
    //        encryptOrdecrypt(false,encrypt,key,null,AESType.AES_128,EncodeType.AES_CFB_PKCS5Padding);
    //        encrypt = encryptOrdecrypt(true,content.getBytes(CHARSET),key,null,AESType.AES_192,EncodeType.AES_CFB_PKCS5Padding);
    //        encryptOrdecrypt(false,encrypt,key,null,AESType.AES_192,EncodeType.AES_CFB_PKCS5Padding);
    //        encrypt = encryptOrdecrypt(true,content.getBytes(CHARSET),key,null,AESType.AES_256,EncodeType.AES_CFB_PKCS5Padding);
    //        encryptOrdecrypt(false,encrypt,key,null,AESType.AES_256,EncodeType.AES_CFB_PKCS5Padding);
    //
    //        /**2.3 AES/CFB
    //         * AES_CFB_NoPadding模式（填充向量可选）
    //         */
    //        System.out.println("【2.3】AES_CFB_NoPadding模式\n");
    //        content = "在线助手";
    //        // 生成密钥需要的密码值
    //        key = "www.it399.com";
    //        encrypt = encryptOrdecrypt(true,content.getBytes(CHARSET),key,null,AESType.AES_128,EncodeType.AES_CFB_ISO10126Padding);
    //        encryptOrdecrypt(false,encrypt,key,null,AESType.AES_128,EncodeType.AES_CFB_ISO10126Padding);
    //        encrypt = encryptOrdecrypt(true,content.getBytes(CHARSET),key,null,AESType.AES_192,EncodeType.AES_CFB_ISO10126Padding);
    //        encryptOrdecrypt(false,encrypt,key,null,AESType.AES_192,EncodeType.AES_CFB_ISO10126Padding);
    //        encrypt = encryptOrdecrypt(true,content.getBytes(CHARSET),key,null,AESType.AES_256,EncodeType.AES_CFB_ISO10126Padding);
    //        encryptOrdecrypt(false,encrypt,key,null,AESType.AES_256,EncodeType.AES_CFB_ISO10126Padding);

  }

  /**
   * 指定一个初始化向量 (Initialization vector，IV)，IV 必须是16位
   */
  private static byte[] getIv() throws Exception {
    return "1234567812345678".getBytes(StandardCharsets.UTF_8);
  }

  private static void log(byte[] encrypt, byte[] decrypt) {
    logger.info("encrypt::{}\ndecrypt::{} origin::{}\n", TypeConvert.bytesToHexString(encrypt), TypeConvert.bytesToHexString(decrypt), new String(decrypt));
  }

  private static void log(String encrypt, String decrypt) {
    logger.info("encrypt::{}\ndecrypt::{}\n", encrypt, decrypt);
  }
}