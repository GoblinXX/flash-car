package com.byy.api.service.vo.home;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author: xcf
 * @Date: 01/07/19 下午 01:49
 * @Description:
 */
@Getter
@Setter
@ToString
public class HomeVO {

  /** 门店id */
  private Long storeId;

  /** 昨日订单数 */
  private Integer yesterdayOrders;

  /** 昨日订单额 */
  private BigDecimal yesterdayAmount;

  /** 总订单数 */
  private Integer totalOrders;

  /** 总订单额 */
  private BigDecimal totalAmount;

  /** 门店累计金额 */
  private BigDecimal cumulativeAmount;

  /** 门店可用金额 */
  private BigDecimal availableAmount;

  /** 日期集合 */
  private List<String> dateList;

  /** 订单数集合(一一对应日期集合) */
  private List<Integer> orderList;

  /** 订单金额集合(一一对应日期集合) */
  private List<BigDecimal> amountList;
}
