package tech.iooo.boot.core.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
}
