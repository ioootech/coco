package tech.iooo.boot.core.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import java.util.Objects;
import lombok.experimental.Delegate;

/**
 * @author 龙也
 * @date 2019/11/7 4:13 下午
 */
public class GsonUtil {

  public static GsonUtil instance = SingletonHolder.gsonUtil;

  private GsonUtil() {
  }

  private static class SingletonHolder {

    static GsonUtil gsonUtil = new GsonUtil();
  }

  @Delegate
  private Gson gson = new GsonBuilder().create();

  public static boolean isValidJsonObject(String jsonStr) {
    JsonElement jsonElement = preCheck(jsonStr);
    if (Objects.isNull(jsonElement)) {
      return false;
    }
    return jsonElement.isJsonObject();
  }

  public static boolean isValidJsonArray(String jsonStr) {
    JsonElement jsonElement = preCheck(jsonStr);
    if (Objects.isNull(jsonElement)) {
      return false;
    }
    return jsonElement.isJsonArray();
  }

  public static boolean isValid(String jsonStr) {
    return isValidJsonObject(jsonStr) || isValidJsonArray(jsonStr);
  }

  private static JsonElement preCheck(String jsonStr) {
    if (StringUtils.isBlank(jsonStr)) {
      return null;
    }
    JsonElement jsonElement;
    try {
      jsonElement = JsonParser.parseString(jsonStr);
    } catch (Exception e) {
      return null;
    }
    return jsonElement;
  }
}
