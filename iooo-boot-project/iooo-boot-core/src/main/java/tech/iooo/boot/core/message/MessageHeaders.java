package tech.iooo.boot.core.message;

import com.google.common.collect.Maps;
import java.io.Serializable;
import java.util.HashMap;
import lombok.Data;

/**
 * Created on 2017/10/24 下午5:33
 *
 * @author Ivan97
 */
@Data
public class MessageHeaders implements Serializable {

    private static final long serialVersionUID = 7035068984269400920L;

    private HashMap<String, Object> headers;

    public MessageHeaders(HashMap<String, Object> headers) {
        this.headers = headers;
    }

    public MessageHeaders() {
        this(Maps.newHashMap());
    }

    @Override
    public String toString() {
        return this.headers.toString();
    }
}
