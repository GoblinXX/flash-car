package com.byy.dal.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: xcf
 * @Date: 18/06/19 下午 01:37
 * @Description:优惠券状态枚举
 */
@Getter
@AllArgsConstructor
public enum  CouponStatus {
  NORMAL(1,"正常"),
  EXPIRED(2,"已过期"),
  UNEXPIRED(3,"未生效"),
  FINISHED(4,"已领完");


  private Integer statusCode;

  private String statusName;
}
