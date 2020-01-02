package com.byy.dal.common.utils.helper;

import net.sourceforge.pinyin4j.PinyinHelper;
import org.apache.logging.log4j.util.Strings;

/**
 * 汉字转拼音工具类
 *
 * @author: yyc
 * @date: 19-4-10 上午10:29
 */
public class PinYinHelper {

  /**
   * 获取汉字首字母大写拼音
   *
   * @param name String
   * @param isSingle 是否只转化第一个汉字
   * @return String
   */
  public static String toHeaderUpperCase(String name, boolean isSingle) {
    if (Strings.isBlank(name)) {
      return name;
    }
    // 不以中文或者英文开头的全部走#
    if (!name.matches("^[\\u4e00-\\u9fa5].*") && !name.matches("^[a-zA-Z].*")) {
      return "#";
    }
    StringBuilder convert = new StringBuilder();
    int length = isSingle ? 1 : name.length();
    for (int j = 0; j < length; j++) {
      char c = name.charAt(j);
      String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(c);
      if (pinyinArray != null) {
        convert.append(pinyinArray[0].charAt(0));
      } else {
        convert.append(c);
      }
    }
    return convert.toString().toUpperCase();
  }

  /**
   * 首字母拼接大写
   *
   * @param name String
   * @return String
   */
  public static String addHeaderUpper(String name) {
    if (Strings.isBlank(name)) {
      return name;
    }
    return toHeaderUpperCase(name, true) + "-" + name;
  }
}
