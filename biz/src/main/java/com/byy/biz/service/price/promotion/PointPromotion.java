package com.byy.biz.service.price.promotion;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * 积分优惠
 *
 * @author: yyc
 * @date: 19-6-18 上午10:05
 */
@Setter
@Getter
@ToString
public class PointPromotion extends Promotion {

  /** 用户输入的积分(需设置) */
  private BigDecimal point = BigDecimal.ZERO;

  public PointPromotion withPoint(BigDecimal point) {
    this.point = point;
    return this;
  }
}
