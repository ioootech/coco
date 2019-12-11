package tech.iooo.boot.core.function;

import java.util.function.Function;

/**
 * Created on 2019-03-28 22:23
 *
 * cascade compose function
 *
 * @author <a href="mailto:yangkizhang@gmail.com?subject=iooo-boot">Ivan97</a>
 */
@FunctionalInterface
public interface CascadeCompose<T> extends Function<Function<T, T>, Function<Function<T, T>, Function<T, T>>> {

}
