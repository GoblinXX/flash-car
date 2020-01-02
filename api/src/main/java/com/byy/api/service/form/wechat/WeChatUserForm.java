package com.byy.api.service.form.wechat;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author: yyc
 * @date: 19-2-27 上午10:59
 */
@Setter
@Getter
@ToString
public class WeChatUserForm {

  /** 偏移量 */
  private String iv;

  /** 加密数据 */
  private String encryptedData;

  /** 凭据 */
  private String code;
}
