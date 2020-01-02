package com.byy.dal.common.utils.helper;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author: yyc
 * @date: 19-3-30 下午5:15
 */
public class Md5Helper {

  /**
   * MD5加密
   *
   * @param txt txt
   * @return String
   */
  public static String md5(String txt) {
    try {
      MessageDigest md = MessageDigest.getInstance("MD5");
      md.update(txt.getBytes());
      byte[] bytes = md.digest();

      int temp;
      StringBuilder builder = new StringBuilder();
      for (byte b : bytes) {
        temp = b;
        if (temp < 0) temp += 256;
        if (temp < 16) builder.append("0");
        builder.append(Integer.toHexString(temp));
      }
      return builder.toString();

    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    }
    return null;
  }
}
