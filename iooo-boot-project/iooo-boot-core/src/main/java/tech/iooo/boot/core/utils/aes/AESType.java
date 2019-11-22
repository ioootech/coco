package tech.iooo.boot.core.utils.aes;

/**
 * @author 龙也
 * @date 2019/11/21 10:36 上午
 */
public enum AESType {
  AES_128(128), AES_192(192), AES_256(256);
  public int value;

  private AESType(int value) {
    this.value = value;
  }

  public int getValue() {
    return value;
  }

  public void setValue(int value) {
    this.value = value;
  }
}
