package com.byy.dal.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: xcf
 * @Date: 20/06/19 下午 02:08
 * @Description:发布类别枚举
 */
@Getter
@AllArgsConstructor
public enum ReleaseType {
  NEW(1,"全新"),
  SECONDHAND(2,"二手");

  private Integer typeCode;

  private String typeString;
}
