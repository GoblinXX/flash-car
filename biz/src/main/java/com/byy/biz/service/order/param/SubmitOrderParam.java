package com.byy.biz.service.order.param;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * 提交订单
 *
 * @author: yyc
 * @date: 19-6-21 上午10:39
 */
@Setter
@Getter
@ToString
public class SubmitOrderParam extends OrderParam {

  /** 是否从购物车下单 */
  private Boolean fromCart = false;

  /** 联系人 */
  private String buyerName;

  /** 电话 */
  private String buyerPhone;

  /** 地址(只针对上门订单) */
  private String address;

  /** 预约时间(只针对上门订单) */
  private LocalDateTime orderTime;

  /** 门店id */
  private Long storeId;

  /** 买家留言 */
  private String message;
}
