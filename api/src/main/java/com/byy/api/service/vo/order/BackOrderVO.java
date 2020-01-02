package com.byy.api.service.vo.order;

import com.byy.dal.enums.OrderStatus;
import com.byy.dal.enums.OrderType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author: xcf
 * @Date: 26/06/19 下午 02:38
 * @Description:
 */
@Getter
@Setter
@ToString
public class BackOrderVO {
  /** 子订单id */
  private Long id;

  /** 主订单id */
  private Long mainOrderId;

  /** 订单号 */
  private String orderNo;

  /** 买家姓名 */
  private String buyerName;

  /** 买家手机号 */
  private String buyerPhone;

  /** 下单skuId(上门或到店代表sku_id,租赁代表rent_product_time_id) */
  private Long skuId;

  /** 下单数量 */
  private Integer amount;

  /** 安装费(上门) */
  private BigDecimal installFee;

  /** 服务费(到店) */
  private BigDecimal serviceFee;

  /** 押金(租赁) */
  private BigDecimal depositFee;

  /** 门店id */
  private Long storeId;

  /** 订单状态 */
  private OrderStatus status;

  /** 订单实付金额 */
  private BigDecimal totalFee;

  /** 商品总价(售价x数量) */
  private BigDecimal goodsFee;

  /** 优惠券折扣 */
  private BigDecimal couponDiscount;

  /** 积分折扣 */
  private BigDecimal pointDiscount;

  /** 使用积分 */
  private BigDecimal usePoint;

  /** 订单地址 */
  private String address;

  /** 订单类型 */
  private OrderType orderType;

  /** 付款时间 */
  private LocalDateTime paidAt;

  /** 预约时间 */
  private LocalDateTime orderTime;

  /** 买家留言 */
  private String message;

  /** 规格名 */
  private String skuName;

  /** 规格售价 */
  private BigDecimal skuPrice;

  /** 规格成本价 */
  private BigDecimal skuCostPrice;

  /** 商品名 */
  private String productName;

  /** 商品主图 */
  private String productPic;

  /** 订单创建时间 */
  private LocalDateTime createdAt;

  /** 门店名 */
  private String storeName;

  /** 分类名 */
  private String categoryName;
}
