package com.byy.biz.service.cartitem;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.byy.dal.entity.beans.cartitem.CartItem;
import com.byy.dal.entity.dos.cartitem.CartItemDO;
import java.util.List;

/**
 * @author: goblin
 * @date: 2019-06-17 15:26:38
 */
public interface CartItemService extends IService<CartItem> {

  /**
   * 小程序分页查询我的购物车
   */
  IPage<CartItemDO> pageByCartItem(Page page, Long userId);

  /**
   * 批量查询所有选择商品信息
   */
  List<CartItemDO> listByCartItemIds(List<Long> ids);
}

