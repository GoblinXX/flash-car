package com.byy.dal.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: xcf
 * @Date: 14/06/19 下午 02:08
 * @Description:积分获取(或扣除)场景枚举
 */
@Getter
@AllArgsConstructor
public enum SceneType {
  COMMENT(1,"评论"),
  PUNCH(2,"签到"),
  NEW_USER_FIRST_SHOPPING(3, "新用户首次消费"),
  REFUND(4, "退款退还积分"),
  CANCEL_ORDER(5,"取消订单"),
  POINT_DEDUCTION(6,"积分抵扣");

  /** 场景编码 */
  private Integer SceneCode;
  /** 场景名 */
  private String SceneName;
}
