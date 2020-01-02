package com.byy.dal.common.utils.helper;

import com.byy.dal.common.errors.BizException;
import com.byy.dal.common.utils.provider.UniqueNoProvider;
import com.byy.dal.enums.OrderStatus;
import com.byy.dal.enums.OrderType;
import lombok.extern.slf4j.Slf4j;

/**
 * 订单辅助类
 *
 * @author: yyc
 * @date: 19-6-26 下午3:21
 */
@Slf4j
public class OrderHelper {

  /**
   * 检查订单流是否正确,订单状态能否过渡
   *
   * @param oldStatus OrderStatus
   * @param newStatus OrderStatus
   * @param orderType OrderType
   */
  public static void checkFlow(OrderStatus oldStatus, OrderStatus newStatus, OrderType orderType) {
    // 作废单可由任意状态过渡,无需检查
    if (newStatus == OrderStatus.CLOSED) {
      return;
    }
    if (orderType == OrderType.STORE || orderType == OrderType.HOME) {
      if (!checkOrderStatus(oldStatus, newStatus)) {
        log.error("常规订单状态无法更新:" + oldStatus + " ---x--> " + newStatus);
        throw new BizException("订单状态无法更新");
      }
    } else if (orderType == OrderType.RENT) {
      if (!checkRentOrderStatus(oldStatus, newStatus)) {
        log.error("租赁订单状态无法更新:" + oldStatus + " ---x--> " + newStatus);
        throw new BizException("订单状态无法更新");
      }
    }
  }

  /**
   * 判断是否为主订单
   *
   * @param orderNo String
   * @return boolean
   */
  public static boolean isMainOrderNo(String orderNo) {
    return orderNo.contains(UniqueNoProvider.UniqueNoType.ES.name())
        && !orderNo.contains(UniqueNoProvider.UniqueNoType.ESO.name());
  }

  /**
   * 检查租赁订单状态能否过渡
   *
   * @param oldStatus OrderStatus
   * @param newStatus OrderStatus
   * @return boolean
   */
  private static boolean checkRentOrderStatus(OrderStatus oldStatus, OrderStatus newStatus) {
    switch (oldStatus) {
      case SUBMITTED:
        return newStatus == OrderStatus.PAID || newStatus == OrderStatus.CANCELLED; // 提交订单要么支付,要么取消
      case PAID:
        return newStatus == OrderStatus.SHIPPED
            || newStatus == OrderStatus.REFUNDING; // 支付完订单后要么发货,要么退款
      case SHIPPED:
        return newStatus == OrderStatus.RECEIVED; // 已发货订单只能确认收货
      case RECEIVED:
        return newStatus == OrderStatus.RETURNING; // 收货(待归还)后,只能归还中
      case REFUNDING:
        return newStatus == OrderStatus.RETURNED; // 归还中只能已归还(待评价)
      case RETURNED:
        return newStatus == OrderStatus.SUCCESS; // 已归还评价,交易完成
      default:
        return false;
    }
  }

  /**
   * 检查上门订单状态能否过渡
   *
   * @param oldStatus OrderStatus
   * @param newStatus OrderStatus
   * @return boolean
   */
  private static boolean checkOrderStatus(OrderStatus oldStatus, OrderStatus newStatus) {
    switch (oldStatus) {
      case SUBMITTED:
        return newStatus == OrderStatus.PAID || newStatus == OrderStatus.CANCELLED; // 提交订单要么支付,要么取消
      case PAID:
        return newStatus == OrderStatus.SHIPPED
            || newStatus == OrderStatus.REFUNDING; // 支付完订单后要么发货,要么退款
      case SHIPPED:
        return newStatus == OrderStatus.RECEIVED; // 已发货订单只能确认收货(待评价)
      case RECEIVED:
        return newStatus == OrderStatus.SUCCESS; // 收货后评价,交易完成
      default:
        return false;
    }
  }
}
