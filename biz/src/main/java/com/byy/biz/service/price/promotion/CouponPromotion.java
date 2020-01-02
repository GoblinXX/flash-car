package com.byy.biz.service.price.promotion;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 优惠券优惠
 *
 * @author: yyc
 * @date: 19-6-18 上午10:04
 */
@Setter
@Getter
@ToString
public class CouponPromotion extends Promotion {

  /** 优惠id(需设置) */
  private Long promotionId;

  public CouponPromotion withPromotionId(Long promotionId) {
    this.promotionId = promotionId;
    return this;
  }
}
