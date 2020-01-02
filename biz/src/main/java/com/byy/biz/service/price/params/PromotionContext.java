package com.byy.biz.service.price.params;

import com.byy.biz.service.price.cart.SkuItem;
import com.byy.biz.service.price.promotion.Promotion;
import com.byy.biz.service.price.view.CouponPromotionView;
import com.byy.dal.enums.PromotionType;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 优惠计算上下文参数
 *
 * @author: yyc
 * @date: 19-6-17 下午6:07
 */
@Setter
@Getter
@ToString
public class PromotionContext {

  /** 用户id(需设置) */
  private Long userId;

  /** sku维度item计算列表(需设置) */
  private List<SkuItem> skuItems;

  /** 订单总价(需设置) */
  private BigDecimal goodsFee;

  /** 订单总成本价(需设置) */
  private BigDecimal costFee;

  /** 订单总服务费(需设置) */
  private BigDecimal serviceFee = BigDecimal.ZERO;

  /** 订单总安装费(需设置) */
  private BigDecimal installFee = BigDecimal.ZERO;

  /** 订单押金费(需设置) */
  private BigDecimal depositFee = BigDecimal.ZERO;

  /** 用户选择的优惠类型(需设置) */
  private Map<PromotionType, Promotion> selectPromotionMap;

  /** 积分兑换比例(需设置,默认1:10) */
  private BigDecimal ratio = BigDecimal.valueOf(10);

  /** 已计算的优惠折扣(计算值) */
  private Map<PromotionType, Promotion> usePromotionMap = Maps.newHashMap();

  /** 前端可显示的优惠券(计算值) */
  private List<CouponPromotionView> couponPromotionViews = Lists.newArrayList();

  /** 前端显示的最大可用积分(计算值) */
  private BigDecimal canUsePoint = BigDecimal.ZERO;

  /** 订单总折扣(计算值) */
  private BigDecimal totalDiscount = BigDecimal.ZERO;

  public PromotionContext withUserId(Long userId) {
    this.userId = userId;
    return this;
  }

  public PromotionContext withSkuItems(List<SkuItem> skuItems) {
    this.skuItems = skuItems;
    return this;
  }

  public PromotionContext withGoodsFee(BigDecimal goodsFee) {
    this.goodsFee = goodsFee;
    return this;
  }

  public PromotionContext withCostFee(BigDecimal costFee) {
    this.costFee = costFee;
    return this;
  }

  public PromotionContext withServiceFee(BigDecimal serviceFee) {
    this.serviceFee = serviceFee;
    return this;
  }

  public PromotionContext withInstallFee(BigDecimal installFee) {
    this.installFee = installFee;
    return this;
  }

  public PromotionContext withDepositFee(BigDecimal depositFee) {
    this.depositFee = depositFee;
    return this;
  }

  public PromotionContext withSelectPromotionMap(Map<PromotionType, Promotion> usePromotions) {
    this.selectPromotionMap = usePromotions;
    return this;
  }

  public PromotionContext withRatio(BigDecimal ratio) {
    this.ratio = ratio;
    return this;
  }

  public PromotionContext withUsePromotionMap(Map<PromotionType, Promotion> usePromotionMap) {
    this.usePromotionMap = usePromotionMap;
    return this;
  }

  public PromotionContext withCouponPromotionViews(List<CouponPromotionView> couponPromotionViews) {
    this.couponPromotionViews = couponPromotionViews;
    return this;
  }

  public PromotionContext withCanUsePoint(BigDecimal canUsePoint) {
    this.canUsePoint = canUsePoint;
    return this;
  }

  public PromotionContext withTotalDiscount(BigDecimal totalDiscount) {
    this.totalDiscount = totalDiscount;
    return this;
  }
}
