package com.byy.dal.common.utils.helper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;

/**
 * @author: yyc
 * @date: 19-1-18 下午3:56
 */
public class JsonHelper {

  private static ObjectMapper objectMapper;

  static {
    objectMapper = new ObjectMapper();
    objectMapper.configure(FAIL_ON_UNKNOWN_PROPERTIES, false);
  }

  /**
   * @param t T
   * @param <T> T
   * @return String
   */
  public static <T> String obj2Str(T t) {
    String str = "";
    try {
      str = objectMapper.writeValueAsString(t);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
    return str;
  }

  /**
   * 将字符串转换为对象
   *
   * @param str str
   * @param cls cls
   * @return T
   */
  public static <T> T str2Obj(String str, Class<T> cls) {
    T object = null;
    try {
      object = objectMapper.readValue(str, cls);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return object;
  }
}
