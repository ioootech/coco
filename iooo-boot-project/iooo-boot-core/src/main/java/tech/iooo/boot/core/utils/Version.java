package tech.iooo.boot.core.utils;

import java.net.URL;
import java.security.CodeSource;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created on 2018/10/18 6:08 PM
 *
 * @author <a href="mailto:yangkizhang@gmail.com?subject=iooo-boot">Ivan97</a>
 */
public final class Version {

  private static final Logger logger = LoggerFactory.getLogger(Version.class);
  private static final String VERSION = getVersion(Version.class, "");

  private static final Map<String, Integer> VERSION2INT = new HashMap<>();

  static {
    // check if there's duplicated jar
    Version.checkDuplicate(Version.class);
  }

  private Version() {
  }


  public static String getVersion() {
    return VERSION;
  }

  public static int getIntVersion(String version) {
    Integer v = VERSION2INT.get(version);
    if (v == null) {
      v = parseInt(version);
      VERSION2INT.put(version, v);
    }
    return v;
  }

  private static int parseInt(String version) {
    int v = 0;
    String[] vArr = version.split("\\.");
    int len = vArr.length;
    for (int i = 0; i < len; i++) {
      v += Integer.parseInt(getDigital(vArr[i])) * Math.pow(10, (len - i - 1) * 2);
    }
    return v;
  }

  private static String getDigital(String v) {
    int index = 0;
    for (int i = 0; i < v.length(); i++) {
      char c = v.charAt(i);
      if (Character.isDigit(c)) {
        if (i == v.length() - 1) {
          index = i + 1;
        } else {
          index = i;
        }
        continue;
      } else {
        index = i;
        break;
      }
    }
    return v.substring(0, index);
  }

  private static boolean hasResource(String path) {
    try {
      return Version.class.getClassLoader().getResource(path) != null;
    } catch (Throwable t) {
      return false;
    }
  }

  public static String getVersion(Class<?> cls, String defaultVersion) {
    try {
      // find version info from MANIFEST.MF first
      String version = cls.getPackage().getImplementationVersion();
      if (version == null || version.length() == 0) {
        version = cls.getPackage().getSpecificationVersion();
      }
      if (version == null || version.length() == 0) {
        // guess version fro jar file namePrefix if nothing's found from MANIFEST.MF
        CodeSource codeSource = cls.getProtectionDomain().getCodeSource();
        if (codeSource == null) {
          logger.info("No codeSource for class " + cls.getName() + " when getVersion, use default version " + defaultVersion);
        } else {
          String file = codeSource.getLocation().getFile();
          if (file != null && file.length() > 0 && file.endsWith(".jar")) {
            file = file.substring(0, file.length() - 4);
            int i = file.lastIndexOf('/');
            if (i >= 0) {
              file = file.substring(i + 1);
            }
            i = file.indexOf("-");
            if (i >= 0) {
              file = file.substring(i + 1);
            }
            while (file.length() > 0 && !Character.isDigit(file.charAt(0))) {
              i = file.indexOf("-");
              if (i >= 0) {
                file = file.substring(i + 1);
              } else {
                break;
              }
            }
            version = file;
          }
        }
      }
      // return default version if no version info is found
      return version == null || version.length() == 0 ? defaultVersion : version;
    } catch (Throwable e) {
      // return default version when any exception is thrown
      logger.error("return default version, ignore exception " + e.getMessage(), e);
      return defaultVersion;
    }
  }

  public static void checkDuplicate(Class<?> cls, boolean failOnError) {
    checkDuplicate(cls.getName().replace('.', '/') + ".class", failOnError);
  }

  public static void checkDuplicate(Class<?> cls) {
    checkDuplicate(cls, false);
  }

  public static void checkDuplicate(String path, boolean failOnError) {
    try {
      // search in caller's classloader
      Enumeration<URL> urls = ClassUtils.getCallerClassLoader(Version.class).getResources(path);
      Set<String> files = new HashSet<String>();
      while (urls.hasMoreElements()) {
        URL url = urls.nextElement();
        if (url != null) {
          String file = url.getFile();
          if (file != null && file.length() > 0) {
            files.add(file);
          }
        }
      }
      // duplicated jar is found
      if (files.size() > 1) {
        String error = "Duplicate class " + path + " in " + files.size() + " jar " + files;
        if (failOnError) {
          throw new IllegalStateException(error);
        } else {
          logger.error(error);
        }
      }
    } catch (Throwable e) {
      logger.error(e.getMessage(), e);
    }
  }
}
