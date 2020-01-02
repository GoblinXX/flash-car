package com.byy.biz.service.order;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.byy.dal.entity.beans.order.Order;
import com.byy.dal.entity.dos.order.BackOrderDO;
import com.byy.dal.entity.dos.order.OrderDO;

import java.util.List;
import java.util.Map;

/** @Author: xcf @Date: 26/06/19 下午 02:13 @Description: */
public interface BackOrderService extends IService<Order> {

  /**
   * 分页获取订单信息
   *
   * @param page
   * @param params
   * @return
   */
  IPage<BackOrderDO> getOrder(IPage page, Map<String, Object> params);

  /**
   * 根据id查询订单详情
   * @param orderId
   * @return
   */
  BackOrderDO getOrderById(Long orderId);

  /**
   * 获取订单信息不分页,导出用
   * @param params
   * @return
   */
  List<BackOrderDO> getOrderExport(Map<String, Object> params);
}
