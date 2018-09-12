package tech.iooo.boot.spring.configuration;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import io.vertx.core.AbstractVerticle;
import lombok.experimental.UtilityClass;

/**
 * Created on 2018/9/3 上午10:10
 *
 * @author <a href="mailto:yangkizhang@gmail.com?subject=iooo-boot">Ivan97</a>
 */
@UtilityClass
public class IoooVerticleServicesHolder {

	public Table<String, String, AbstractVerticle> verticleServices = HashBasedTable.create();
}
