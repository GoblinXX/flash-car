package com.byy.api.service.vo.coupon;

import com.byy.dal.enums.CouponStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author: xcf
 * @Date: 15/06/19 下午 02:04
 * @Description:
 */
@Getter
@Setter
@ToString
public class CouponVO {
  /** 优惠券id */
  private Long id;

  /** 优惠券名 */
  private String name;

  /** 面值 */
  private BigDecimal faceValue;

  /** 数量 */
  private Integer amount;

  /** 剩余数量 */
  private Integer restAmount;

  /** 开始时间 */
  private LocalDateTime validFrom;

  /** 结束时间 */
  private LocalDateTime validTo;

  /** 使用条件 */
  private BigDecimal conditionUse;

  /** 是否拥有 */
  private Boolean possession;

  /** 优惠券状态 */
  private CouponStatus couponStatus;

  /** 优惠券状态字段 */
  private String couponStatusStr;

  public String getCouponStatusStr(){
    return couponStatus != null ? couponStatus.getStatusName() : null;
  }
}
