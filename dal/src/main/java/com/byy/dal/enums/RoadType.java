package com.byy.dal.enums;

import lombok.Getter;

/**
 * @program: flash-car
 * @description: 道路救援订单状态
 * @author: Goblin
 * @create: 2019-06-18 15:02
 **/
@Getter
public enum RoadType {
  /**
   * 待付款-SUBMITTED
   */
  SUBMITTED("SUBMITTED", "待付款"),
  /**
   * 待接单-PAID
   */
  PAID("PAID", "待接单"),
  /**
   * 待处理-PROCESSING
   */
  PROCESSING("PROCESSING", "待处理"),
  /**
   * 已完成-SUCCESS
   */
  SUCCESS("SUCCESS", "已完成"),
  /**
   * 已取消-CANCELLED
   */
  CANCELLED("CANCELLED", "已取消"),
  /**
   * 退款中-REFUNDING
   */
  REFUNDING("REFUNDING", "退款中"),
  /**
   * 已退款-REFUNDED
   */
  REFUNDED("REFUNDED", "已退款"),
  /**
   * 已关闭-CLOSED
   */
  CLOSED("CLOSED", "已关闭");


  private String code;
  private String desc;

  RoadType(String code, String desc) {
    this.code = code;
    this.desc = desc;
  }
}
