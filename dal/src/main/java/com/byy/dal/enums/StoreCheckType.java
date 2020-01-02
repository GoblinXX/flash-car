package com.byy.dal.enums;

import lombok.Getter;

/**
 * @program: flash-car
 * @description: 门店申请状态
 * @author: Goblin
 * @create: 2019-06-11 16:15
 **/
@Getter
public enum StoreCheckType {
  /**
   * 审核中
   */
  AUDITING("AUDITING", "申请中"),
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

  StoreCheckType(String code, String desc) {
    this.code = code;
    this.desc = desc;
  }
}
