package tech.iooo.boot.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.lang.reflect.Type;
import java.util.Objects;
import java.util.Optional;
import lombok.SneakyThrows;
import lombok.experimental.Delegate;

/**
 * @author 龙也
 * @date 2020/4/25 9:34 下午
 */
public class JacksonUtils {

  private JacksonUtils() {
  }

  public static final JacksonUtils instance = SingletonHolder.instance;

  private static class SingletonHolder {

    public static JacksonUtils instance = new JacksonUtils();
  }

  @Delegate
  private final ObjectMapper objectMapper = new ObjectMapper();

  public void init(JacksonConfig config) {
    if (Objects.nonNull(config)) {
      Optional.ofNullable(config.getSerializationConfig()).ifPresent(instance.objectMapper::setConfig);
      Optional.ofNullable(config.getDeserializationConfig()).ifPresent(instance.objectMapper::setConfig);
      Optional.ofNullable(config.getDateFormat()).ifPresent(objectMapper::setDateFormat);
    }
  }

  @SneakyThrows
  public static <T> String toJson(T t) {
    return instance.objectMapper.writeValueAsString(t);
  }

  @SneakyThrows
  public static <T> T fromJson(String json, Class<T> tClass) {
    return instance.objectMapper.readValue(json, tClass);
  }

  @SneakyThrows
  public static <T> T fromJson(String json, Type type) {
    return instance.objectMapper.readValue(json, instance.objectMapper.getTypeFactory().constructType(type));
  }
}
