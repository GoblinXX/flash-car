package com.byy.dal.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 订单类型
 *
 * @author: yyc
 * @date: 19-6-17 下午5:40
 */
@AllArgsConstructor
@Getter
public enum OrderType {

  /** 到店安装 */
  STORE(1, "到店安装"),

  /** 上门服务 */
  HOME(2, "上门服务"),

  /** 道路救援 */
  ROAD_RESCUE(3, "道路救援"),

  /** 租赁订单 */
  RENT(4, "租赁订单");

  private int code;

  private String desc;
}
