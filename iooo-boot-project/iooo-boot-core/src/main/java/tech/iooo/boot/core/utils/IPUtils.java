package tech.iooo.boot.core.utils;

import com.google.common.base.Strings;

/**
 * Created on 2017/11/14 下午4:26
 *
 * @author Ivan97
 */
public class IPUtils {

  public static boolean isInRange(String network, String mask) {
    if (Strings.isNullOrEmpty(mask)) {
      throw new NullPointerException("IP段不能为空！");
    }
    if (Strings.isNullOrEmpty(network)) {
      throw new NullPointerException("IP不能为空！");
    }
    mask = mask.trim();
    network = network.trim();
    final String regxIp = "((25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]\\d|\\d)\\.){3}(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]\\d|\\d)";
    if (!network.matches(regxIp)) {
      return false;
    }
    String[] networkips = network.split("\\.");
    int ipAddr = (Integer.parseInt(networkips[0]) << 24)
        | (Integer.parseInt(networkips[1]) << 16)
        | (Integer.parseInt(networkips[2]) << 8)
        | Integer.parseInt(networkips[3]);
    int type = Integer.parseInt(mask.replaceAll(".*/", ""));
    int mask1 = 0xFFFFFFFF << (32 - type);
    String maskIp = mask.replaceAll("/.*", "");
    String[] maskIps = maskIp.split("\\.");
    int cidrIpAddr = (Integer.parseInt(maskIps[0]) << 24)
        | (Integer.parseInt(maskIps[1]) << 16)
        | (Integer.parseInt(maskIps[2]) << 8)
        | Integer.parseInt(maskIps[3]);

    return (ipAddr & mask1) == (cidrIpAddr & mask1);
  }
}
