package com.byy.api.security.wechat;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * @author: yyc
 * @date: 19-6-10 上午11:10
 */
@Setter
@Getter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WeChatAccessTokenData {

  /** 令牌 */
  @JsonProperty("access_token")
  private String accessToken;

  /** 过期时间 */
  @JsonProperty("expires_in")
  private Integer expiresIn;

  /** 错误码 */
  @JsonProperty("errcode")
  private Integer errCode;

  /** 错误信息 */
  @JsonProperty("errmsg")
  private String errMsg;

  /** 实际过期时间除以2 */
  public LocalDateTime getExpiredAt() {
    return LocalDateTime.ofInstant(Instant.ofEpochMilli(expiresIn / 2), ZoneId.of("+8"));
  }
}
