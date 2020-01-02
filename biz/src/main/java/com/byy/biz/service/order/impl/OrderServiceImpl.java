package com.byy.biz.service.order.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.byy.dal.common.utils.provider.WrapperProvider;
import com.byy.dal.entity.beans.order.Order;
import com.byy.dal.entity.beans.roadrescueorder.RoadRescueOrder;
import com.byy.dal.entity.dos.home.HomeDO;
import com.byy.dal.entity.dos.order.OrderDO;
import com.byy.dal.enums.RoadType;
import com.byy.dal.mapper.order.BackOrderMapper;
import com.byy.dal.mapper.order.OrderMapper;
import com.byy.biz.service.order.OrderService;
import com.byy.dal.mapper.roadrescueorder.RoadRescueOrderMapper;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

import static com.byy.dal.enums.OrderStatus.*;
import static com.byy.dal.enums.RoadType.PROCESSING;

/**
 * @author: yyc
 * @date: 2019-06-24 14:10:46
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

  @Autowired private OrderMapper orderMapper;

  @Autowired private RoadRescueOrderMapper roadRescueOrderMapper;

  @Autowired private BackOrderMapper backOrderMapper;

  @Override
  public Integer getYesterdayOrders(Long storeId) {
    LocalDate now = LocalDateTime.now().toLocalDate();
    LocalDate yesterday = now.minusDays(1);
    LambdaQueryWrapper<Order> orderWrapper = getOrderWrapper(storeId, yesterday, now);
    LambdaQueryWrapper<RoadRescueOrder> roadWrapper = getRoadWrapper(storeId, yesterday, now);
    return orderMapper.selectCount(orderWrapper) + roadRescueOrderMapper.selectCount(roadWrapper);
  }

  @Override
  public BigDecimal getYesterdayAmount(Long storeId) {
    LocalDate now = LocalDateTime.now().toLocalDate();
    LocalDate yesterday = now.minusDays(1);
    LambdaQueryWrapper<RoadRescueOrder> roadWrapper = getRoadWrapper(storeId, yesterday, now);
    LambdaQueryWrapper<Order> orderWrapper = getOrderWrapper(storeId, yesterday, now);
    BigDecimal orderYesterdayAmount =
        orderMapper.selectList(orderWrapper).stream()
            .map(Order::getTotalFee)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    BigDecimal roadYesterdayAmount =
        roadRescueOrderMapper.selectList(roadWrapper).stream()
            .map(RoadRescueOrder::getTotalFee)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    return orderYesterdayAmount.add(roadYesterdayAmount);
  }

  @Override
  public Integer getTotalOrders(Long storeId) {
    LambdaQueryWrapper<RoadRescueOrder> roadWrapper = getRoadWrapper(storeId, null, null);
    LambdaQueryWrapper<Order> orderWrapper = getOrderWrapper(storeId, null, null);
    return orderMapper.selectCount(orderWrapper) + roadRescueOrderMapper.selectCount(roadWrapper);
  }

  @Override
  public BigDecimal getTotalAmount(Long storeId) {
    LambdaQueryWrapper<RoadRescueOrder> roadWrapper = getRoadWrapper(storeId, null, null);
    LambdaQueryWrapper<Order> orderWrapper = getOrderWrapper(storeId, null, null);
    return roadRescueOrderMapper.selectList(roadWrapper).stream()
        .map(RoadRescueOrder::getTotalFee)
        .reduce(BigDecimal.ZERO, BigDecimal::add)
        .add(
            orderMapper.selectList(orderWrapper).stream()
                .map(Order::getTotalFee)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
  }

  @Override
  public Map<String, Object> getChartData(Long storeId, LocalDate startTime, LocalDate endTime) {
    Map<String, Object> chartDataMap = Maps.newHashMap();
    List<HomeDO> orderHomeDOS = backOrderMapper.getOrderToChart(storeId, startTime, endTime);
    List<HomeDO> roadOrderHomeDOS =
        roadRescueOrderMapper.getRoadOrderToChart(storeId, startTime, endTime);
    List<String> dateList =
        orderHomeDOS.stream().map(HomeDO::getCountDate).collect(Collectors.toList());
    // order表取出的每天的订单数
    List<Integer> orderList =
        orderHomeDOS.stream().map(HomeDO::getOrders).collect(Collectors.toList());
    // roadOrder表取出的每天的订单数
    List<Integer> roadOrderList =
        roadOrderHomeDOS.stream().map(HomeDO::getOrders).collect(Collectors.toList());
    // 将order每天的订单数与roadOrder每天的订单数相加
    List<Integer> totalOrdersList =
        IntStream.range(0, orderList.size())
            .map(i -> orderList.get(i) + roadOrderList.get(i))
            .boxed()
            .collect(Collectors.toList());
    // order表取出的每天的订单总额
    List<BigDecimal> orderAmountList =
        orderHomeDOS.stream().map(HomeDO::getOrderAmount).collect(Collectors.toList());
    // roadOrder表取出每天的订单总额
    List<BigDecimal> roadOrderAmountList =
        roadOrderHomeDOS.stream().map(HomeDO::getOrderAmount).collect(Collectors.toList());
    // 将order表每天的订单额与roadOrder表每天的订单额相加存入totalOrderAmountList
    List<BigDecimal> totalOrderAmountList = new ArrayList<>();
    for (int i = 0; i < orderAmountList.size(); i++) {
      totalOrderAmountList.add(orderAmountList.get(i).add(roadOrderAmountList.get(i)));
    }
    chartDataMap.put("dateList", dateList);
    chartDataMap.put("orderList", totalOrdersList);
    chartDataMap.put("amountList", totalOrderAmountList);
    return chartDataMap;
  }

  @Override
  public List<HomeDO> getChartDataExport(Long storeId, LocalDate startTime, LocalDate endTime) {
    List<HomeDO> orderHomeDOS = backOrderMapper.getOrderToChart(storeId, startTime, endTime);
    List<HomeDO> roadOrderHomeDOS =
        roadRescueOrderMapper.getRoadOrderToChart(storeId, startTime, endTime);
    LongAdder adder = new LongAdder();
    orderHomeDOS.stream()
        .forEach(
            oh -> {
              HomeDO homeDO = roadOrderHomeDOS.get(adder.intValue());
              oh.setOrderAmount(homeDO.getOrderAmount().add(oh.getOrderAmount()));
              oh.setOrders(homeDO.getOrders() + oh.getOrders());
              adder.increment();
            });
    return orderHomeDOS;
  }

  /**
   * 包装order表条件
   *
   * @param storeId
   * @param yesterday
   * @param now
   * @return
   */
  private LambdaQueryWrapper<Order> getOrderWrapper(
      Long storeId, LocalDate yesterday, LocalDate now) {
    LambdaQueryWrapper<Order> orderWrapper = Wrappers.<Order>lambdaQuery();
    if (storeId != 0) {
      orderWrapper.eq(Order::getStoreId, storeId);
    }
    if (yesterday != null && now != null) {
      orderWrapper.between(Order::getCreatedAt, yesterday, now);
    }
    orderWrapper.and(
        R ->
            R.eq(Order::getStatus, SHIPPED)
                .or()
                .eq(Order::getStatus, RECEIVED)
                .or()
                .eq(Order::getStatus, RETURNING)
                .or()
                .eq(Order::getStatus, RETURNED)
                .or()
                .eq(Order::getStatus, SUCCESS));
    return orderWrapper;
  }

  /**
   * 包装Road表条件
   *
   * @param storeId
   * @param yesterday
   * @param now
   * @return
   */
  private LambdaQueryWrapper<RoadRescueOrder> getRoadWrapper(
      Long storeId, LocalDate yesterday, LocalDate now) {
    LambdaQueryWrapper<RoadRescueOrder> roadWrapper = Wrappers.<RoadRescueOrder>lambdaQuery();
    if (yesterday != null && now != null) {
      roadWrapper.between(RoadRescueOrder::getCreatedAt, yesterday, now);
    }
    if (storeId != 0) {
      roadWrapper.eq(RoadRescueOrder::getStoreId, storeId);
    }
    roadWrapper.and(
        R ->
            R.eq(RoadRescueOrder::getStatus, PROCESSING)
                .or()
                .eq(RoadRescueOrder::getStatus, RoadType.SUCCESS));
    return roadWrapper;
  }
}
