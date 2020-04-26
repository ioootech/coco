package tech.iooo.boot.json;

import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.SerializationConfig;
import java.text.DateFormat;
import lombok.Data;

/**
 * @author 龙也
 * @date 2020/4/25 9:35 下午
 */
@Data
public class JacksonConfig {

  private SerializationConfig serializationConfig;
  private DeserializationConfig deserializationConfig;
  private DateFormat dateFormat;
}
