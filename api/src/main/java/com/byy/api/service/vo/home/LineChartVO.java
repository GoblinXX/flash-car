package com.byy.api.service.vo.home;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * @Author: xcf
 * @Date: 03/07/19 上午 10:00
 * @Description:后台首页折线图VO(用于导出)
 */
@Getter
@Setter
@ToString
public class LineChartVO {

  /** 日期 */
  private String countDate;

  /** 订单数 */
  private Integer orders;

  /** 订单额 */
  private BigDecimal orderAmount;
}
