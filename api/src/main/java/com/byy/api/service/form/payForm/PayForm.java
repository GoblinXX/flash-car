package com.byy.api.service.form.payForm;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * @Author: xcf
 * @Date: 04/07/19 下午 03:17
 * @Description:
 */
@Getter
@Setter
@ToString
public class PayForm {

  /** 退押金金额 */
  private BigDecimal depositAmount;

  /** 子订单编号 */
  private String orderNo;

}
