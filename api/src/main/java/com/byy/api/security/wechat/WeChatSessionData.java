package com.byy.api.security.wechat;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author: yyc
 * @date: 19-6-10 上午11:00
 */
@Setter
@Getter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WeChatSessionData {

  /** 三方openId */
  @JsonProperty("openid")
  private String openId;

  /** 三方unionId */
  @JsonProperty("unionid")
  private String unionId;

  /** 三方sessionKey */
  @JsonProperty("session_key")
  private String sessionKey;

  /** 错误码（0代表请求成功） */
  @JsonProperty("errcode")
  private Integer errCode;

  /** 错误信息 */
  @JsonProperty("errmsg")
  private String errMsg;
}
