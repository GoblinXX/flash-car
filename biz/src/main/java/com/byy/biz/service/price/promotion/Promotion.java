package com.byy.biz.service.price.promotion;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * 优惠
 *
 * @author: yyc
 * @date: 19-6-19 上午10:34
 */
@Setter
@Getter
@ToString
public abstract class Promotion {

  /** 当前计算的优惠(计算值) */
  private BigDecimal promotionDiscount = BigDecimal.ZERO;

  public Promotion withPromotionDiscount(BigDecimal promotionDiscount) {
    this.promotionDiscount = promotionDiscount;
    return this;
  }
}
