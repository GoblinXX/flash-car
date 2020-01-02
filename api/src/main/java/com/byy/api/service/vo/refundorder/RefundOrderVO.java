package com.byy.api.service.vo.refundorder;

import com.byy.dal.enums.OrderType;
import com.byy.dal.enums.RefundStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.type.Alias;

/**
 * @program: flash-car
 * @description: 退款订单实体类
 * @author: Goblin
 * @create: 2019-06-21 16:10
 **/
@Alias("RefundOrderVO")
@Getter
@Setter
@ToString
public class RefundOrderVO {

  /**
   * 主键id
   */
  private Long id;

  /**
   * 用户userId
   */
  private Long userId;
  /**
   * 门店id
   */
  private Long storeId;

  /**
   * 退款订单号
   */
  private String outRefundNo;
  /**
   * 关联订单号
   */
  private String orderNo;
  /**
   * 关联主订单号(用于退款)
   */
  private String mainOrderNo;
  /**
   * 订单状态
   */
  private RefundStatus status;
  /**
   * 退款金额
   */
  private BigDecimal amount;
  /**
   * 订单类型
   */
  private OrderType orderType;
  /**
   * 门店名称
   */
  private String storeName;
  /**
   * 门店主图
   */
  private String storeImage;
  /**
   * 商品名称
   */
  private String productName;
  /**
   * 买家姓名
   */
  private String buyerName;
  /**
   * 买家电话
   */
  private String buyerPhone;
  /**
   * 收货地址
   */
  private String address;
  /**
   * 下单时间
   */
  private LocalDateTime orderAt;
  /**
   * 商品主图
   */
  private String productImage;
  /**
   * 商品数量
   */
  private Integer total;
  /**
   * 规格名称
   */
  private String skuName;
  /**
   * 订单总金额
   */
  private BigDecimal goodsFee;

}
