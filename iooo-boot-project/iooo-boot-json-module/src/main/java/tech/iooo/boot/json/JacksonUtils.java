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

  public static final JacksonUtils INSTANCE = SingletonHolder.INSTANCE;

  private static class SingletonHolder {

    public static JacksonUtils INSTANCE = new JacksonUtils();
  }

  @Delegate
  private final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

  public void init(JacksonConfig config) {
    if (Objects.nonNull(config)) {
      Optional.ofNullable(config.getSerializationConfig()).ifPresent(INSTANCE.OBJECT_MAPPER::setConfig);
      Optional.ofNullable(config.getDeserializationConfig()).ifPresent(INSTANCE.OBJECT_MAPPER::setConfig);
      Optional.ofNullable(config.getDateFormat()).ifPresent(OBJECT_MAPPER::setDateFormat);
    }
  }

  @SneakyThrows
  public static <T> String toJson(T t) {
    return INSTANCE.OBJECT_MAPPER.writeValueAsString(t);
  }

  @SneakyThrows
  public static <T> T fromJson(String json, Class<T> tClass) {
    return INSTANCE.OBJECT_MAPPER.readValue(json, tClass);
  }

  @SneakyThrows
  public static <T> T fromJson(String json, Type type) {
    return INSTANCE.OBJECT_MAPPER.readValue(json, INSTANCE.OBJECT_MAPPER.getTypeFactory().constructType(type));
  }
}
