package com.byy.biz.service.order.param;

import com.byy.biz.service.price.cart.SkuItem;
import com.byy.dal.enums.OrderType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.List;

/**
 * 订单参数
 *
 * @author: yyc
 * @date: 19-6-20 上午11:02
 */
@Setter
@Getter
@ToString
public class OrderParam {

  /** sku下单列表 */
  private List<SkuItem> skuItems;

  /** 优惠券id(用户选择优惠券) */
  private Long couponId;

  /** 使用积分(用户输入积分) */
  private BigDecimal usePoint;

  /** 用户id */
  private Long userId;

  /** 订单类型 */
  private OrderType orderType;

  /** 是否续租 */
  private Boolean rentAgain = false;

  /** 续租的原订单id */
  private Long originOrderId;

  /** 续租的原主订单号 */
  private String originMainOrderNo;
}
