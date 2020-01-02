package com.byy.dal.enums;

import lombok.Getter;

/**
 * @program: flash-car
 * @description: 佣金类型
 * @author: Goblin
 * @create: 2019-06-14 10:21
 **/
@Getter
public enum CommissionType {
  /**
   * 下级消费获得
   */
  GET("GET", "下级消费获得"),
  /**
   * 提现
   */
  CASH("CASH", "提现");


  private String code;
  private String desc;

  CommissionType(String code, String desc) {
    this.code = code;
    this.desc = desc;
  }

}
