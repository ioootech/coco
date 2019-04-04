package tech.iooo.boot.core.utils;

/**
 * Created on 2018/10/18 6:04 PM
 *
 * @author <a href="mailto:yangkizhang@gmail.com?subject=iooo-boot">Ivan97</a>
 */
public abstract class IoooUtils {

  /**
   * line separator
   */
  public static final String LINE_SEPARATOR = System.getProperty("line.separator");


  /**
   * The separator of property namePrefix
   */
  public static final String PROPERTY_NAME_SEPARATOR = ".";

  /**
   * The prefix of property namePrefix of Iooo
   */
  public static final String IOOO_PREFIX = "iooo";

  /**
   * The prefix of property namePrefix for Iooo scan
   */
  public static final String IOOO_SCAN_PREFIX = IOOO_PREFIX + PROPERTY_NAME_SEPARATOR + "scan";

  /**
   * The prefix of property namePrefix for Iooo Config.ØØ
   */
  public static final String IOOO_CONFIG_PREFIX = IOOO_PREFIX + PROPERTY_NAME_SEPARATOR + "config";

  /**
   * The property namePrefix of base packages to scan
   * <p>
   * The default value is empty set.
   */
  public static final String BASE_PACKAGES_PROPERTY_NAME = IOOO_SCAN_PREFIX + PROPERTY_NAME_SEPARATOR + "basePackages";

  /**
   * The property namePrefix of multiple properties binding from externalized configuration
   * <p>
   * The default value is {@link #DEFAULT_MULTIPLE_CONFIG_PROPERTY_VALUE}
   */
  public static final String MULTIPLE_CONFIG_PROPERTY_NAME = IOOO_CONFIG_PREFIX + PROPERTY_NAME_SEPARATOR + "multiple";

  /**
   * The default value of multiple properties binding from externalized configuration
   */
  public static final boolean DEFAULT_MULTIPLE_CONFIG_PROPERTY_VALUE = false;

  /**
   * The property namePrefix of override Iooo config
   * <p>
   * The default value is {@link #DEFAULT_OVERRIDE_CONFIG_PROPERTY_VALUE}
   */
  public static final String OVERRIDE_CONFIG_PROPERTY_NAME = IOOO_CONFIG_PREFIX + PROPERTY_NAME_SEPARATOR + "override";

  /**
   * The default property value of  override Iooo config
   */
  public static final boolean DEFAULT_OVERRIDE_CONFIG_PROPERTY_VALUE = true;

}
