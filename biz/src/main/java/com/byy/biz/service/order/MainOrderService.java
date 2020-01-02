package com.byy.biz.service.order;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.byy.biz.service.order.param.OrderParam;
import com.byy.biz.service.order.param.SubmitOrderParam;
import com.byy.biz.service.price.params.PromotionContext;
import com.byy.dal.entity.beans.order.MainOrder;
import com.byy.dal.entity.beans.order.Order;
import com.byy.dal.entity.dos.order.OrderDO;
import com.byy.dal.enums.OrderStatus;

import java.util.Map;

/**
 * @author: yyc
 * @date: 2019-06-20 10:39:12
 */
public interface MainOrderService extends IService<MainOrder> {

  /**
   * 确认订单
   *
   * @param param OrderParam
   * @return PromotionContext
   */
  PromotionContext confirmOrder(OrderParam param);

  /**
   * 提交订单
   *
   * @param param SubmitOrderParam
   * @return MainOrder
   */
  MainOrder submitOrder(SubmitOrderParam param);

  /**
   * 取消订单
   *
   * @param orderNo String
   * @param userId Long
   * @return MainOrder
   */
  MainOrder cancelOrder(String orderNo, Long userId);

  /**
   * 支付回调
   *
   * @param orderNo String
   */
  void asyncPayBack(String orderNo);

  /**
   * 查询订单列表
   *
   * @param page IPage
   * @param params Map
   * @return IPage
   */
  IPage<OrderDO> loadOrders(IPage<OrderDO> page, Map<String, Object> params);

  /**
   * 确认收货,归还
   *
   * @param orderNo String
   * @param newStatus OrderStatus
   * @param userId
   * @return Order
   */
  Order modifyOrderStatus(String orderNo, OrderStatus newStatus, Long userId);

  /**
   * 发送短信
   */
  void sendSms(String orderNo);
}
