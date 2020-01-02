package com.byy.dal.entity.beans.security;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author: yyc
 * @date: 19-3-30 下午5:11
 */
@Setter
@Getter
@ToString
public class SecurityProperties {

  /** jwt相关配置 */
  private Jwt jwt;

  /** 登录默认路径 */
  private String loginUrl;

  /** 登录成功跳转路径 */
  private String loginSuccessUrl;

  /** 登录失败跳转路径 */
  private String loginFailUrl;

  /** 允许不带token访问的路径 */
  private String[] permitUrls;
}
