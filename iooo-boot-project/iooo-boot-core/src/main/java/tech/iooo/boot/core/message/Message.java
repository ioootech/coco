package tech.iooo.boot.core.message;

import java.io.Serializable;

/**
 * Created on 2017/10/24 下午5:33
 *
 * @author Ivan97
 */
public interface Message<T> extends Serializable {
    /**
     * Return the message payload.
     */
    T getPayload();

    /**
     * Return message headers for the message (never {@code null} but may be empty).
     */
    MessageHeaders getHeaders();
}
