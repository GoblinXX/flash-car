package com.byy.api.service.form.order;

import com.byy.dal.enums.OrderStatus;
import com.byy.dal.enums.OrderType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author: yyc
 * @date: 19-6-26 上午10:26
 */
@Setter
@Getter
@ToString
public class ListOrderForm {

  /** 订单状态(为空代表查询所有状态) */
  private OrderStatus status;

  /** 订单类型 */
  private OrderType orderType;
}
