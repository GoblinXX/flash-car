package com.byy.dal.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 系统相关异常
 *
 * @author: yyc
 * @date: 19-3-30 下午4:59
 */
@Getter
@AllArgsConstructor
public enum GlobalErrorCode {

  // --------------------系统返回码----------------------
  SUCCESS(1, "成功"),
  INTERNAL_ERROR(-1, "系统繁忙,请稍后再试"),

  // --------------------jwtToken相关----------------------
  INVALID_TOKEN_FORMAT(101, "token格式不正确"),
  TOKEN_EXPIRED(102, "token已失效"),
  USER_NOT_FOUND(103, "不存在该用户"),
  USER_AUTH_ERROR(104, "用户登录验证失败"),
  USER_CREATE_ERROR(105, "用户注册失败"),
  PHONE_USER_EXISTS(106, "该手机用户已存在"),

  // --------------------微信登录配置相关----------------------
  CONFIG_NOT_FOUND(501, "没有找到配置信息"),
  WE_CHAT_USER_UPDATE_ERROR(502, "微信用户信息更新失败"),
  ACCESS_TOKEN_UPDATE_ERROR(503, "微信accessToken更新失败"),
  CODE_NULL_ERROR(504, "临时票据code不能为空"),
  WE_CHAT_OPEN_ID_NULL_ERROR(505, "微信登录验证失败"),
  WE_CHAT_USER_INFO_NULL_ERROR(506, "微信授权信息返回失败"),

  // --------------------登录相关----------------------------
  PASSWORD_NULL_ERROR(1000, "密码不能为空"),
  SMS_CODE_NULL_ERROR(1001, "验证码不能为空"),
  USER_PHONE_NULL_ERROR(1002, "手机号不能为空"),
  USER_NAME_NULL_ERROR(1003, "用户名不能为空");

  /** 错误码 */
  private int code;

  /** 错误信息 */
  private String message;
}
