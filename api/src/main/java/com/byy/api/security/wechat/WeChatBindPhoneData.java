package com.byy.api.security.wechat;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;

/**
 * @author: yyc
 * @date: 19-6-10 上午11:03
 */
@Setter
@Getter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WeChatBindPhoneData {

  /** 用户绑定的手机号 */
  @JsonProperty("phoneNumber")
  private String phone;

  /** 没有区号的手机号 */
  private String purePhoneNumber;

  /** 区号 */
  private Integer countryCode;

  /** 时间和appId */
  private Map<String, Object> watermark;
}
