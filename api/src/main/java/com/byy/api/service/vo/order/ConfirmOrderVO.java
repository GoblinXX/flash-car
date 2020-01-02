package com.byy.api.service.vo.order;

import com.byy.api.service.vo.address.UserAddressVO;
import com.byy.biz.service.price.view.CouponPromotionView;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.List;

/**
 * 确认订单展示
 *
 * @author: yyc
 * @date: 19-6-20 下午2:14
 */
@Setter
@Getter
@ToString
public class ConfirmOrderVO {

  /** 地址,优先展示默认地址,如果没有默认地址,展示第一条 */
  private UserAddressVO userAddressVO;

  /** 可使用优惠券 */
  private List<CouponPromotionView> couponPromotionViews;

  /** 可抵扣积分 */
  private BigDecimal canUsePoint;

  /** 可抵扣积分折扣 */
  private BigDecimal canUsePointDiscount;

  /** 服务费 */
  private BigDecimal serviceFee;

  /** 安装费 */
  private BigDecimal installFee;

  /** 押金 */
  private BigDecimal depositFee;

  /** 商品金额 */
  private BigDecimal goodsFee;

  /** 订单合计 */
  private BigDecimal totalFee;

  /** 优惠券折扣 */
  private BigDecimal couponDiscount = BigDecimal.ZERO;

  /** 积分折扣 */
  private BigDecimal pointDiscount = BigDecimal.ZERO;

  public ConfirmOrderVO withUserAddressVO(UserAddressVO userAddressVO) {
    this.userAddressVO = userAddressVO;
    return this;
  }

  public ConfirmOrderVO withCouponPromotionViews(List<CouponPromotionView> couponPromotionViews) {
    this.couponPromotionViews = couponPromotionViews;
    return this;
  }

  public ConfirmOrderVO withCanUsePoint(BigDecimal canUsePoint) {
    this.canUsePoint = canUsePoint;
    return this;
  }

  public ConfirmOrderVO withCanUsePointDiscount(BigDecimal canUsePointDiscount) {
    this.canUsePointDiscount = canUsePointDiscount;
    return this;
  }

  public ConfirmOrderVO withGoodsFee(BigDecimal goodsFee) {
    this.goodsFee = goodsFee;
    return this;
  }

  public ConfirmOrderVO withServiceFee(BigDecimal serviceFee) {
    this.serviceFee = serviceFee;
    return this;
  }

  public ConfirmOrderVO withInstallFee(BigDecimal installFee) {
    this.installFee = installFee;
    return this;
  }

  public ConfirmOrderVO withDepositFee(BigDecimal depositFee) {
    this.depositFee = depositFee;
    return this;
  }

  public ConfirmOrderVO withCouponDiscount(BigDecimal couponDiscount) {
    this.couponDiscount = couponDiscount;
    return this;
  }

  public ConfirmOrderVO withPointDiscount(BigDecimal pointDiscount) {
    this.pointDiscount = pointDiscount;
    return this;
  }

  public ConfirmOrderVO withTotalFee(BigDecimal totalFee) {
    this.totalFee = totalFee;
    return this;
  }
}
