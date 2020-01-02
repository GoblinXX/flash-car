package com.byy.biz.service.price.detail;

import com.byy.dal.enums.PromotionType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * 每个sku的优惠折扣详情
 *
 * @author: yyc
 * @date: 19-6-18 上午11:24
 */
@Setter
@Getter
@ToString
public class SkuPromotionDetail {

  /** 优惠类型(计算值) */
  private PromotionType promotionType;

  /** 分摊后的折扣(计算值) */
  private BigDecimal promotionTotalDiscount;

  public SkuPromotionDetail withPromotionType(PromotionType promotionType) {
    this.promotionType = promotionType;
    return this;
  }

  public SkuPromotionDetail withPromotionTotalDiscount(BigDecimal promotionTotalDiscount) {
    this.promotionTotalDiscount = promotionTotalDiscount;
    return this;
  }
}
