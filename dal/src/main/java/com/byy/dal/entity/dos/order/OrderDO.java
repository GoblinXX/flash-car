package com.byy.dal.entity.dos.order;

import com.byy.dal.entity.beans.order.Order;
import com.byy.dal.entity.beans.order.OrderSku;
import com.byy.dal.enums.OrderDimension;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: yyc
 * @date: 2019-06-26 09:30:09
 */
@Setter
@Getter
@ToString
public class OrderDO extends Order {

  /** 订单维度 */
  private OrderDimension orderDimension;

  /** 门店名称 */
  private String storeName;

  /** 门店主图 */
  private String storeImage;

  /** 门店地址 */
  private String storeAddress;

  /** 订单sku列表 */
  List<OrderSku> orderSkus = new ArrayList<>();
}
