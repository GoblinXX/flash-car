package com.byy.dal.entity.beans.roadrescueorder;

import com.byy.dal.entity.beans.base.BaseEntityArchiveWithUserId;
import com.byy.dal.enums.OrderType;
import com.byy.dal.enums.RoadType;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.type.Alias;

/**
 * @program: flash-car
 * @description: 道路救援订单
 * @author: Goblin
 * @create: 2019-06-18 14:55
 **/
@Alias("RoadRescueOrder")
@Getter
@Setter
@ToString
public class RoadRescueOrder extends BaseEntityArchiveWithUserId {

  /**
   * 支付时间
   */
  private LocalDateTime paidAt;
  /**
   * 商品总价
   */
  private BigDecimal goodsFee;
  /**
   * 实付金额
   */
  private BigDecimal totalFee;
  /**
   * 买家姓名
   */
  private String buyerName;
  /**
   * 买家电话
   */
  private String buyerPhone;
  /**
   * 订单编号
   */
  private String orderNo;
  /**
   * 门店id
   */
  private Long storeId;
  /**
   * 订单状态
   */
  private RoadType status;
  /**
   * 选择地址
   */
  private String address;
  /**
   * 详细地址
   */
  private String detailedAddress;
  /**
   * 服务类型
   */
  private OrderType orderType;
  /**
   * 道路救援商品id
   */
  private Long roadRescueId;
  /**
   * 备注
   */
  private String remarks;
  /**
   * 是否展示
   */
  private Boolean showBack;

}
