package com.byy.dal.entity.dos.cash;

import com.byy.dal.entity.beans.cash.Cash;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.type.Alias;

/**
 * @program: flash-car
 * @description:
 * @author: Goblin
 * @create: 2019-06-14 17:27
 **/
@Setter
@Getter
@ToString
@Alias("CashDO")
public class CashDO extends Cash {

  /**
   * 昵称
   */
  private String nickname;

  /**
   * 头像
   */
  private String avatar;
  /**
   * 手机号
   */
  private String phone;
}
