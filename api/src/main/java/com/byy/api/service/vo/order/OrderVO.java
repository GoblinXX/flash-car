package com.byy.api.service.vo.order;

import com.byy.dal.entity.beans.order.OrderSku;
import com.byy.dal.enums.OrderDimension;
import com.byy.dal.enums.OrderStatus;
import com.byy.dal.enums.OrderType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 订单
 *
 * @author: yyc
 * @date: 19-6-24 上午10:47
 */
@Setter
@Getter
@ToString
public class OrderVO {

  /** 主键id */
  private Long id;

  /** 用户userId */
  private Long userId;

  /** 订单号 */
  private String orderNo;

  /** 原主订单号(针对租赁商品有用) */
  private String originMainOrderNo;

  /** 原订单id(针对租赁) */
  private Long originOrderId;

  /** 买家姓名 */
  private String buyerName;

  /** 买家手机号 */
  private String buyerPhone;

  /** 安装费(上门) */
  private BigDecimal installFee;

  /** 服务费(到店) */
  private BigDecimal serviceFee;

  /** 押金(租赁) */
  private BigDecimal depositFee;

  /** 订单实付金额 */
  private BigDecimal totalFee;

  /** 商品总价(售价x数量) */
  private BigDecimal goodsFee;

  /** 门店id */
  private Long storeId;

  /** 订单地址 */
  private String address;

  /** 订单状态 */
  private OrderStatus status;

  /** 优惠券折扣 */
  private BigDecimal couponDiscount;

  /** 积分折扣 */
  private BigDecimal pointDiscount;

  /** 使用积分 */
  private BigDecimal usePoint;

  /** 订单类型 */
  private OrderType orderType;

  /** 付款时间 */
  private LocalDateTime paidAt;

  /** 下单时间 */
  private LocalDateTime createdAt;

  /** 预约时间 */
  private LocalDateTime orderTime;

  /** 买家留言 */
  private String message;

  /** 订单维度 */
  private OrderDimension orderDimension;

  /** 门店名称 */
  private String storeName;

  /** 门店主图 */
  private String storeImage;

  /** 门店地址 */
  private String storeAddress;

  /** 订单sku列表 */
  List<OrderSku> orderSkus = new ArrayList<>();
}
