package com.byy.dal.entity.beans.order;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * 订单商品
 *
 * @author: yyc
 * @date: 19-6-26 上午9:32
 */
@Setter
@Getter
@ToString
public class OrderSku {

  /** skuId */
  private Long skuId;

  /** 下单数量 */
  private Integer amount;

  /** 规格名 */
  private String skuName;

  /** 规格售价 */
  private BigDecimal skuPrice;

  /** 规格成本价 */
  private BigDecimal skuCostPrice;

  /** 商品名 */
  private String productName;

  /** 商品主图 */
  private String productPic;
}
