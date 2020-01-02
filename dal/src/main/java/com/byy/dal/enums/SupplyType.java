package com.byy.dal.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: xcf
 * @Date: 20/06/19 下午 02:03
 * @Description:供求类型枚举
 */
@Getter
@AllArgsConstructor
public enum SupplyType {
  SUPPLY(1,"供货"),
  BUYING(2,"求购");

  /** 类型编号 */
  private Integer typeCode;

  /** 类型名 */
  private String typeName;
}
