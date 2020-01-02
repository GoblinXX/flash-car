package com.byy.dal.enums;

import lombok.Getter;

/**
 * @program: flash-car
 * @description: 退款状态
 * @author: Goblin
 * @create: 2019-06-24 11:03
 **/
@Getter
public enum RefundStatus {
  /**
   * 待处理
   */
  AUDITING("AUDITING", "待处理"),
  /**
   * 已同意
   */
  SUCCESS("SUCCESS", "已同意"),
  /**
   * 已拒绝
   */
  FAILED("FAILED", "已拒绝");

  private String code;
  private String desc;

  RefundStatus(String code, String desc) {
    this.code = code;
    this.desc = desc;
  }
}
