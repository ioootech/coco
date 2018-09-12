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

	private static final Table<String, String, AbstractVerticle> VERTICLE_SERVICES = HashBasedTable.create();

	public static Table<String, String, AbstractVerticle> verticleServices() {
		synchronized (VERTICLE_SERVICES) {
			return VERTICLE_SERVICES;
		}
	}
}
