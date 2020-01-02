package com.byy.dal.entity.dos.home;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.type.Alias;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author: xcf
 * @Date: 02/07/19 上午 10:09
 * @Description:后台首页折线图数据实体类
 */
@Getter
@Setter
@ToString
@Alias("HomeDO")
public class HomeDO {

  /** 日期 */
  private String countDate;

  /** 订单数 */
  private Integer orders;

  /** 订单额 */
  private BigDecimal orderAmount;
}
