package com.byy.api.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author: yyc
 * @date: 19-3-30 下午5:40
 */
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AuthTypeJwtInfo {

  /** 用户id */
  private Long id;

  /** 用户类型 */
  private String type;

  /** MD5加密摘要 */
  private String signature;
}
