package tech.iooo.boot.core.constants;

import com.google.common.base.Strings;
import lombok.experimental.UtilityClass;

/**
 * Created on 2018-12-18 10:06
 *
 * @author <a href="mailto:yangkizhang@gmail.com?subject=iooo-boot">Ivan97</a>
 */
@UtilityClass
public class SystemProperties {

  public String CVS_PASSFILE = getProperty("CVS_PASSFILE");
  public String FILE_ENCODING = getProperty("file.encoding");
  public String FILE_ENCODING_PKG = getProperty("file.encoding.pkg");
  public String FILE_SEPARATOR = getProperty("file.separator");
  public String FTP_NON_PROXY_HOSTS = getProperty("ftp.nonProxyHosts",
      "192.168.0.0/16|*.192.168.0.0/16|10.0.0.0/8|*.10.0.0.0/8|172.16.0.0/12|*.172.16.0.0/12|127.0.0.1|localhost|*.localhost|local|*.local");
  public boolean GOPHER_PROXY_SET = getPropertyAsBoolean(System.getProperty("gopherProxySet"), false);
  public String HTTP_NON_PROXY_HOSTS = getProperty("http.nonProxyHosts",
      "192.168.0.0/16|*.192.168.0.0/16|10.0.0.0/8|*.10.0.0.0/8|172.16.0.0/12|*.172.16.0.0/12|127.0.0.1|localhost|*.localhost|local|*.local");
  public String HTTP_PROXY_HOST = getProperty("http.proxyHost");
  public int HTTP_PROXY_PORT = getPropertyAsInteger("http.proxyPort");
  public String HTTPS_PROXY_HOST = getProperty("https.proxyHost");
  public int HTTPS_PROXY_PORT = getPropertyAsInteger("https.proxyPort");
  public String JAVA_CLASS_PATH = getProperty("java.class.path");
  public String JAVA_CLASS_VERSION = getProperty("java.class.version");
  public String JAVA_ENDORSED_DIRS = getProperty("java.endorsed.dirs");
  public String JAVA_EXT_DIRS = getProperty("java.ext.dirs");
  public String JAVA_HOME = getProperty("java.home");
  public String JAVA_IO_TMPDIR = getProperty("java.io.tmpdir");
  public String JAVA_LIBRARY_PATH = getProperty("java.library.path");
  public boolean JAVA_RMI_SERVER_DISABLE_HTTP = getPropertyAsBoolean("java.rmi.server.disableHttp", true);
  public String JAVA_RMI_SERVER_HOSTNAME = getProperty("java.rmi.server.hostname");
  public String JAVA_RUNTIME_NAME = getProperty("java.runtime.namePrefix");
  public String JAVA_RUNTIME_VERSION = getProperty("java.runtime.version");
  public String JAVA_SPECIFICATION_NAME = getProperty("java.specification.namePrefix");
  public String JAVA_SPECIFICATION_VENDOR = getProperty("java.specification.vendor");
  public String JAVA_SPECIFICATION_VERSION = getProperty("java.specification.version");
  public boolean JAVA_UTIL_ARRAYS_USE_LEGACY_MERGE_SORT = getPropertyAsBoolean("java.util.Arrays.useLegacyMergeSort", true);
  public String JAVA_UTIL_CONCURRENT_FORK_JOIN_POOL_COMMON_THREAD_FACTORY = getProperty("java.util.concurrent.ForkJoinPool.common.threadFactory");
  public String JAVA_VENDOR = getProperty("java.vendor");
  public String JAVA_VERSION = getProperty("java.version");
  public String JAVA_VM_INFO = getProperty("java.vm.info");
  public String JAVA_VM_NAME = getProperty("java.vm.namePrefix");
  public String JAVA_VM_SPECIFICATION_NAME = getProperty("java.vm.specification.namePrefix");
  public String JAVA_VM_SPECIFICATION_VENDOR = getProperty("java.vm.specification.vendor");
  public String JAVA_VM_SPECIFICATION_VERSION = getProperty("java.vm.specification.version");
  public String JAVA_VM_VENDOR = getProperty("java.vm.vendor");
  public String JAVA_VM_VERSION = getProperty("java.vm.version");
  public boolean JAVAX_SWING_REBASE_CSS_SIZE_MAP = getPropertyAsBoolean("javax.swing.rebaseCssSizeMap", true);
  public String LINE_SEPARATOR = System.lineSeparator();
  public String OS_ARCH = getProperty("os.arch");
  public String OS_NAME = getProperty("os.namePrefix");
  public String OS_VERSION = getProperty("os.version");
  public String PATH_SEPARATOR = getProperty("path.separator");
  public String SOCKS_NON_PROXY_HOSTS = getProperty("socksNonProxyHosts","192.168.0.0/16|*.192.168.0.0/16|10.0.0.0/8|*.10.0.0.0/8|172.16.0.0/12|*.172.16.0.0/12|127.0.0.1|localhost|*.localhost|local|*.local");
  public String SOCKS_PROXY_HOST = getProperty("socksProxyHost");
  public int SOCKS_PROXY_PORT = getPropertyAsInteger("socksProxyPort"); 
  public String SUN_ARCH_DATA_MODEL = getProperty("sun.arch.data.model");
  public String USER_COUNTRY = getProperty("user.country");
  public String USER_DIR = getProperty("user.dir");
  public String USER_HOME = getProperty("user.home");
  public String USER_LANGUAGE = getProperty("user.language");
  public String USER_LANGUAGE_FORMAT = getProperty("user.language.format");
  public String USER_NAME = getProperty("user.namePrefix");
  public String USER_TIMEZONE = getProperty("user.timezone");

  private String getProperty(String key) {
    return System.getProperty(key);
  }

  private String getProperty(String key, String def) {
    return System.getProperty(key, def);
  }

  private boolean getPropertyAsBoolean(String key) {
    return Boolean.getBoolean(System.getProperty(key));
  }

  private boolean getPropertyAsBoolean(String key, boolean def) {
    return Strings.isNullOrEmpty(System.getProperty(key)) ? def : Boolean.getBoolean(System.getProperty(key));
  }

  private int getPropertyAsInteger(String key) {
    return Integer.parseInt(System.getProperty(key));
  }

  private int getPropertyAsInteger(String key, int def) {
    return Strings.isNullOrEmpty(System.getProperty(key)) ? def : Integer.parseInt(System.getProperty(key));
  }
}
