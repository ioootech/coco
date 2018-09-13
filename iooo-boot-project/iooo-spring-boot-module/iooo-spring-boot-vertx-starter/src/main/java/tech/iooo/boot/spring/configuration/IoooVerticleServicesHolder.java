package tech.iooo.boot.spring.configuration;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import io.vertx.core.AbstractVerticle;

/**
 * Created on 2018/9/3 上午10:10
 *
 * @author <a href="mailto:yangkizhang@gmail.com?subject=iooo-boot">Ivan97</a>
 */
public class IoooVerticleServicesHolder {

  private static final Table<String, String, AbstractVerticle> ACTIVE_VERTICLE_SERVICES = HashBasedTable.create();
  private static final Table<String, String, AbstractVerticle> INACTIVE_VERTICLE_SERVICES = HashBasedTable.create();


  public static Table<String, String, AbstractVerticle> activeVerticleServices() {
    synchronized (IoooVerticleServicesHolder.class) {
      return ACTIVE_VERTICLE_SERVICES;
    }
  }

  public static Table<String, String, AbstractVerticle> inactiveVerticleServices() {
    synchronized (IoooVerticleServicesHolder.class) {
      return INACTIVE_VERTICLE_SERVICES;
    }
  }
}
