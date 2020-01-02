package com.byy.api.service.vo.cartitem;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.type.Alias;

/**
 * @program: flash-car
 * @description: 购物车
 * @author: Goblin
 * @create: 2019-06-17 15:21
 **/
@Alias("CartItemVO")
@Getter
@Setter
@ToString
public class CartItemVO {

  /**
   * 主键id
   */
  private Long id;

  /**
   * 用户userId
   */
  private Long userId;
  /**
   * skuId
   */
  private Long skuId;
  /**
   * 购买数量
   */
  private Integer quantity;
  /**
   * 商品主图
   */
  private String image;
  /**
   * 商品名称
   */
  private String productName;
  /**
   * 规格名称
   */
  private String name;
  /**
   * 商品售价
   */
  private BigDecimal salePrice;

}
