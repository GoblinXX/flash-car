package com.byy.api.service.vo.sys;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * 后台用户
 *
 * @author: yyc
 * @date: 19-5-15 上午11:55
 */
@Setter
@Getter
@ToString
public class SysUserVO {

  /** 主键id */
  private Long id;

  /** 门店id */
  private Long storeId;

  /** 用户姓名 */
  private String name;

  /** 用户名(用户账号) */
  private String username;

  /** 密码 */
  private String password;

  /** 用户权限 */
  private List<String> sysAuthorities;
}
