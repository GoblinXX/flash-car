package com.byy.dal.enums;

import lombok.Getter;

/**
 * @program: flash-car
 * @description: 提现状态
 * @author: Goblin
 * @create: 2019-06-14 16:49
 **/
@Getter
public enum CashType {
  /**
   * 审核中
   */
  PENDING("PENDING", "申请中"),
  /**
   * 审核通过
   */
  SUCCESS("SUCCESS", "已同意"),
  /**
   * 审核失败
   */
  FAILED("FAILED", "已拒绝");

  private String code;
  private String desc;

  CashType(String code, String desc) {
    this.code = code;
    this.desc = desc;
  }
}
