package com.byy.dal.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: xcf
 * @Date: 15/06/19 下午 04:08
 * @Description:该用户优惠券状态枚举
 */
@Getter
@AllArgsConstructor
public enum UserCouponStatus {
  NORMAL(1,"可使用"),
  EXPIRED(2,"已过期"),
  USED(3,"已使用"),
  NO_DATE_USE(4,"未生效");

  /** 状态码 */
  private Integer statusId;
  /** 状态字段 */
  private String statusName;
}