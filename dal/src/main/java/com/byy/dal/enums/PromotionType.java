package com.byy.dal.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 优惠类型
 *
 * @author: yyc
 * @date: 19-6-17 下午5:58
 */
@AllArgsConstructor
@Getter
public enum PromotionType {

  /** 优惠券 */
  COUPON(1, "优惠券"),

  /** 积分 */
  POINT(2, "积分");

  private int code;

  private String desc;
}
