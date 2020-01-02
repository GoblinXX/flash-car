package com.byy.biz.service.price.view;

import com.byy.dal.entity.dos.coupon.UserCouponDO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 前端优惠券展示
 *
 * @author: yyc
 * @date: 19-6-18 下午2:03
 */
@Setter
@Getter
@ToString
public class CouponPromotionView {

  /** 优惠券id(计算值) */
  private Long promotionId;

  /** 优惠券名称(计算值) */
  private String promotionName;

  /** 优惠券使用条件(满多少) (计算值) */
  private BigDecimal conditionPrice;

  /** 优惠券面值(计算值) */
  private BigDecimal discount;

  /** 优惠券开始(计算值) */
  private LocalDateTime validFrom;

  /** 优惠券结束(计算值) */
  private LocalDateTime validTo;

  public CouponPromotionView withPromotionId(Long promotionId) {
    this.promotionId = promotionId;
    return this;
  }

  public CouponPromotionView withPromotionName(String promotionName) {
    this.promotionName = promotionName;
    return this;
  }

  public CouponPromotionView withConditionPrice(BigDecimal conditionPrice) {
    this.conditionPrice = conditionPrice;
    return this;
  }

  public CouponPromotionView withDiscount(BigDecimal discount) {
    this.discount = discount;
    return this;
  }

  public CouponPromotionView withValidFrom(LocalDateTime validFrom) {
    this.validFrom = validFrom;
    return this;
  }

  public CouponPromotionView withValidTo(LocalDateTime validTo) {
    this.validTo = validTo;
    return this;
  }

  /**
   * 转化前端可显示的优惠
   *
   * @param userCouponDO UserCouponDO
   * @return CouponPromotionView
   */
  public static CouponPromotionView withUserCoupon(UserCouponDO userCouponDO) {
    return new CouponPromotionView()
        .withPromotionId(userCouponDO.getId())
        .withPromotionName(userCouponDO.getCouponName())
        .withConditionPrice(userCouponDO.getConditionUse())
        .withDiscount(userCouponDO.getFaceValue())
        .withValidFrom(userCouponDO.getValidFrom())
        .withValidTo(userCouponDO.getValidTo());
  }
}
