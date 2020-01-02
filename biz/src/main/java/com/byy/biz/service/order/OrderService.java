package com.byy.biz.service.order;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.byy.dal.entity.beans.order.Order;
import com.byy.dal.entity.dos.home.HomeDO;
import com.byy.dal.entity.dos.order.OrderDO;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * @author: yyc
 * @date: 2019-06-24 14:10:46
 */
public interface OrderService extends IService<Order> {

  /**
   * 获取昨日订单数
   * @param storeId
   * @return
   */
  Integer getYesterdayOrders(Long storeId);

  /**
   * 获取昨日订单总额
   * @param storeId
   * @return
   */
  BigDecimal getYesterdayAmount(Long storeId);

  /**
   * 获取总订单数
   * @param storeId
   * @return
   */
  Integer getTotalOrders(Long storeId);

  /**
   * 获取总订单额
   * @param storeId
   * @return
   */
  BigDecimal getTotalAmount(Long storeId);

  /**
   * 获取后台首页图表数据
   * @param storeId
   * @param startTime
   * @param endTime
   * @return
   */
  Map<String,Object> getChartData(Long storeId, LocalDate startTime, LocalDate endTime);

  /**
   * 获取折线图数据导出用
   * @param storeId
   * @param startTime
   * @param endTime
   * @return
   */
  List<HomeDO> getChartDataExport(Long storeId, LocalDate startTime, LocalDate endTime);
}
