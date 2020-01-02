package com.byy.dal.entity.dos.cartitem;

import com.byy.dal.entity.beans.cartitem.CartItem;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.type.Alias;

/**
 * @author: goblin
 * @date: 2019-06-17 15:26:38
 */
@Setter
@Getter
@ToString
@Alias("CartItemDO")
public class CartItemDO extends CartItem {

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
