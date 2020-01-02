package com.byy.dal.entity.beans.wechat;

import com.byy.dal.entity.beans.base.BaseEntityArchive;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * 微信配置
 *
 * @author: yyc
 * @date: 19-3-30 下午4:29
 */
@Setter
@Getter
@ToString
public class WeChatConfig extends BaseEntityArchive {

  /** 三方appId */
  private String appId;

  /** 三方登录密钥 */
  private String appSecret;

  /** 三方登录token */
  private String accessToken;

  /** token过期时间 */
  private LocalDateTime expiredAt;
}
