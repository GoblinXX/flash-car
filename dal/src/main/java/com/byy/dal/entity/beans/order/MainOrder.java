package com.byy.dal.entity.beans.order;

import com.byy.dal.entity.beans.base.BaseEntityArchiveWithUserId;
import com.byy.dal.enums.OrderStatus;
import com.byy.dal.enums.OrderType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 主订单
 *
 * @author: yyc
 * @date: 19-6-20 上午10:21
 */
@Setter
@Getter
@ToString
public class MainOrder extends BaseEntityArchiveWithUserId {

  /** 主订单号 */
  private String mainOrderNo;

  /** 买家姓名 */
  private String buyerName;

  /** 买家手机号 */
  private String buyerPhone;

  /** 买家留言 */
  private String message;

  /** 优惠券折扣 */
  private BigDecimal couponDiscount;

  /** 优惠券id */
  private Long couponId;

  /** 积分折扣 */
  private BigDecimal pointDiscount;

  /** 使用积分 */
  private BigDecimal usePoint;

  /** 订单状态 */
  private OrderStatus status;

  /** 门店id */
  private Long storeId;

  /** 订单实付金额 */
  private BigDecimal totalFee;

  /** 商品总价(售价x数量) */
  private BigDecimal goodsFee;

  /** 订单类型 */
  private OrderType orderType;

  /** 收货地址 */
  private String address;

  /** 安装费 */
  private BigDecimal installFee;

  /** 服务费 */
  private BigDecimal serviceFee;

  /** 押金 */
  private BigDecimal depositFee;

  /** 预约时间 */
  private LocalDateTime orderTime;

  /** 付款时间 */
  private LocalDateTime paidAt;

  /** 是否为续租订单 */
  private Boolean rentAgain;
}
