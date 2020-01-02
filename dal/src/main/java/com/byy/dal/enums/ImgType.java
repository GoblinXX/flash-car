package com.byy.dal.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: xcf
 * @Date: 20/06/19 下午 02:19
 * @Description:论坛图片类型枚举
 */
@Getter
@AllArgsConstructor
public enum ImgType {
  COVER(1,"封面"),
  DETAIL(2,"详情图");

  /** 类型编号 */
  private Integer typeCode;

  /** 类型名 */
  private String typeName;
}
