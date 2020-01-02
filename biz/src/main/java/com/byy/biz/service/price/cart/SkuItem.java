package com.byy.biz.service.price.cart;

import com.byy.biz.service.price.detail.SkuPromotionDetail;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.List;

/**
 * 每个子单的详情(sku维度)
 *
 * @author: yyc
 * @date: 19-6-18 下午3:11
 */
@Setter
@Getter
@ToString
public class SkuItem {

  /** skuId(需设置) */
  private Long skuId;

  /** 下单数量(需设置) */
  private BigDecimal amount;

  /** sku售价(需设置) */
  private BigDecimal price;

  /** sku成本价(需设置) */
  private BigDecimal costPrice;

  /** 规格名(需设置) */
  private String skuName;

  /** 商品名(需设置) */
  private String productName;

  /** 商品主图(需设置) */
  private String productPic;

  /** 服务费 */
  private BigDecimal serviceFee = BigDecimal.ZERO;

  /** 安装费 */
  private BigDecimal installFee = BigDecimal.ZERO;

  /** 押金 */
  private BigDecimal depositFee = BigDecimal.ZERO;

  /** sku分摊完后的总价(需设置,初始值与price相同,动态计算值) */
  private BigDecimal itemTotalPrice;

  /** sku分摊后的总折扣(计算值) */
  private BigDecimal promotionTotalDiscount = BigDecimal.ZERO;

  /** 每个sku分摊使用的优惠类型列表(计算值) */
  private List<SkuPromotionDetail> skuPromotionDetails = Lists.newArrayList();

  public SkuItem withSkuId(Long skuId) {
    this.skuId = skuId;
    return this;
  }

  public SkuItem withAmount(BigDecimal amount) {
    this.amount = amount;
    return this;
  }

  public SkuItem withPrice(BigDecimal price) {
    this.price = price;
    return this;
  }

  public SkuItem withCostPrice(BigDecimal costPrice) {
    this.costPrice = costPrice;
    return this;
  }

  public SkuItem withServiceFee(BigDecimal serviceFee) {
    this.serviceFee = serviceFee;
    return this;
  }

  public SkuItem withInstallFee(BigDecimal installFee) {
    this.installFee = installFee;
    return this;
  }

  public SkuItem withDepositFee(BigDecimal depositFee) {
    this.depositFee = depositFee;
    return this;
  }

  public SkuItem withItemTotalPrice(BigDecimal promotionTotalPrice) {
    this.itemTotalPrice = promotionTotalPrice;
    return this;
  }

  public SkuItem withPromotionTotalDiscount(BigDecimal promotionTotalDiscount) {
    this.promotionTotalDiscount = promotionTotalDiscount;
    return this;
  }

  public SkuItem withSkuPromotionDetails(List<SkuPromotionDetail> skuPromotionDetails) {
    this.skuPromotionDetails = skuPromotionDetails;
    return this;
  }

  public SkuItem withSkuName(String skuName) {
    this.skuName = skuName;
    return this;
  }

  public SkuItem withProductName(String productName) {
    this.productName = productName;
    return this;
  }

  public SkuItem withProductPic(String productPic) {
    this.productPic = productPic;
    return this;
  }
}
