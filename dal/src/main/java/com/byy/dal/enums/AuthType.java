package com.byy.dal.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用户类型
 *
 * @author: yyc
 * @date: 19-3-30 下午4:59
 */
@Getter
@AllArgsConstructor
public enum AuthType {

  /** 后台用户 */
  SYS(1, "sys"),

  /** 微信用户 */
  WECHAT(2, "wechat"),

  /**
   * 手机密码用户
   */
  PHONE(3, "phone"),

  /** 未知用户 */
  ERROR(-1, "error_type");

  /** 用户类型 */
  private int code;

  /** 用户类型描述 */
  private String desc;

  public static AuthType of(String type) {
    for (AuthType value : values()) {
      if (value.desc.equals(type)) {
        return value;
      }
    }
    return ERROR;
  }
}
