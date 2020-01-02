package com.byy.dal.entity.beans.cartitem;

import com.byy.dal.entity.beans.base.BaseEntityArchiveWithUserId;
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
@Alias("CartItem")
@Getter
@Setter
@ToString
public class CartItem extends BaseEntityArchiveWithUserId {

  /**
   * skuId
   */
  private Long skuId;
  /**
   * 购买数量
   */
  private Integer quantity;
}
