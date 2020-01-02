package com.byy.dal.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: xcf
 * @Date: 19/06/19 下午 02:11
 * @Description:轮播图跳转类型
 */
@Getter
@AllArgsConstructor
public enum JumpType {

  IMAGE(1,"图片"),
  PRODUCT(2,"商品"),
  NOTHING(3,"无跳转");


  /** 类型编号 */
  private Integer typeCode;

  /** 类型名称 */
  private String typeName;
}
