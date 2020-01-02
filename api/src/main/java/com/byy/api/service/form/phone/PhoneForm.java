package com.byy.api.service.form.phone;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @program: flash-car
 * @description: 手机用户
 * @author: Goblin
 * @create: 2019-06-26 14:27
 **/
@Getter
@Setter
@ToString
public class PhoneForm {

  /**
   * 手机号
   */
  private String phone;
  /**
   * 密码
   */
  private String password;
}
