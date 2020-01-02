package com.byy.dal.entity.beans.refundorder;

import com.byy.dal.entity.beans.base.BaseEntityArchiveWithUserId;
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
@Alias("RefundOrder")
@Getter
@Setter
@ToString
public class RefundOrder extends BaseEntityArchiveWithUserId {

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
   * 门店id
   */
  private Long storeId;
  /**
   * 下单时间
   */
  private LocalDateTime orderAt;
  /**
   * 订单总金额
   */
  private BigDecimal goodsFee;

}
