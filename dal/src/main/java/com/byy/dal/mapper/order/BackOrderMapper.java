package com.byy.dal.mapper.order;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.byy.dal.entity.beans.order.Order;
import com.byy.dal.entity.dos.home.HomeDO;
import com.byy.dal.entity.dos.order.BackOrderDO;
import com.byy.dal.entity.dos.order.OrderDO;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * @Author: xcf
 * @Date: 26/06/19 下午 02:02
 * @Description:
 */
public interface BackOrderMapper extends BaseMapper<Order> {

  /**
   * 分页获取订单信息
   * @param page
   * @param params
   * @return
   */
  IPage<BackOrderDO> getOrder(@Param("page") IPage page, @Param("params") Map<String, Object> params);

  /**
   * 根据id查询订单详情
   * @param orderId
   * @return
   */
  BackOrderDO getOrderById(@Param("orderId") Long orderId);

  /**
   * 获取订单信息不分页,导出用
   * @param params
   * @return
   */
  List<BackOrderDO> getOrderExport(@Param("params") Map<String, Object> params);

  /**
   * 获取日期,订单数,订单额用于后台首页折线图
   * @param storeId
   * @param startTime
   * @param endTime
   * @return
   */
  List<HomeDO> getOrderToChart(@Param("storeId") Long storeId, @Param("startTime") LocalDate startTime, @Param("endTime")LocalDate endTime);
}
