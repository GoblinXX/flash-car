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
public class Jwt {

  /** token密钥 */
  private String secure;

  /** token过期时间 */
  private Long expireTime;

  /** token头名称 */
  private String header;
}
