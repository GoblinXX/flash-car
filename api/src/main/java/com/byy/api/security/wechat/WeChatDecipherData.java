package com.byy.api.security.wechat;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;

/**        
 * @author: yyc
 * @date: 19-6-10 上午11:26
 */
@Setter
@Getter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WeChatDecipherData {

  /** 三方openId */
  private String openId;

  /** 三方unionId */
  private String unionId;

  /** 昵称 */
  @JsonProperty("nickName")
  private String nickname;

  /** 性别 */
  private Integer gender;

  /** 市 */
  private String city;

  /** 省 */
  private String province;

  /** 国 */
  private String country;

  /** 头像 */
  @JsonProperty("avatarUrl")
  private String avatar;

  /** 语言 */
  private String language;

  /** 时间和appId */
  private Map<String, Object> watermark;
}
