package com.byy.dal.enums;

/**
 * 租赁订单(RENT)状态(SUBMITTED待付款/PAID待发货/SHIPPED待收货/RECEIVED待归还/RETURNING归还中/RETURNED归还完成,待评价/SUCCESS交易完成)
 *
 * <p>上门订单(HOME)状态(SUBMITTED待付款/PAID待上门/SHIPPED待完成/RECEIVED待评价/SUCCESS交易完成)
 *
 * <p>到店订单(STORE)状态(SUBMITTED待付款/PAID待发货/SHIPPED待收货/RECEIVED待评价/SUCCESS交易完成)
 *
 * @author: yyc
 * @date: 19-6-20 上午10:02
 */
public enum OrderStatus {

  /** 已提交(待付款) */
  SUBMITTED,

  /** 已付款(待发货) */
  PAID,

  /** 已发货(待收货) */
  SHIPPED,

  /** 已收货(确认收货,待评价,待归还[针对租赁订单]) */
  RECEIVED,

  /** 归还中(针对租赁订单) */
  RETURNING,

  /** 已归还(针对租赁订单,后台退押金,待评价) */
  RETURNED,

  /** 交易完成(已评价) */
  SUCCESS,

  /** 取消 */
  CANCELLED,

  /** 退款中 */
  REFUNDING,

  /** 交易关闭 */
  CLOSED//暂时用不到
}