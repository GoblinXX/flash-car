package com.byy.api.service.vo.coupon;

import com.byy.dal.enums.UserCouponStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author: xcf
 * @Date: 15/06/19 下午 02:05
 * @Description:
 */
@Getter
@Setter
@ToString
public class UserCouponVO {

  /** 用户优惠券id */
  private Long id;

  /** 优惠券id */
  private Long couponId;

  /** 用户id */
  private Long userId;

  /** 是否可用 */
  private Boolean usable;

  /** 面值 */
  private BigDecimal faceValue;

  /** 开始时间 */
  private LocalDateTime validFrom;

  /** 结束时间 */
  private LocalDateTime validTo;

  /** 使用条件 */
  private BigDecimal conditionUse;

  /** 优惠券删除状态 */
  private Boolean couponArchive;

  /** 优惠券状态 */
  private UserCouponStatus userCouponStatus;

  /** 优惠券状态字段 */
  private String userCouponStatusStr;

  public String getUserCouponStatusStr(){
    return userCouponStatus !=null ? userCouponStatus.getStatusName() : null;
  }

}
