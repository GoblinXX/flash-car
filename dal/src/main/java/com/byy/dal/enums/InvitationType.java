package com.byy.dal.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: xcf
 * @Date: 20/06/19 下午 01:56
 * @Description:帖子类型枚举
 */
@Getter
@AllArgsConstructor
public enum InvitationType {

  PLAY(1,"吃喝玩乐"),
  CLOTHES(2,"帽鞋服饰"),
  OFFICE(3,"居家办公"),
  TRAVEL(4,"安全出行");

  /** 类型编号 */
  private Integer typeCode;

  /** 类型名 */
  private String typeName;
}
