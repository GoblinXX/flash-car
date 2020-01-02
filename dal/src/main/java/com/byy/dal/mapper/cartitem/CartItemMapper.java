package com.byy.dal.mapper.cartitem;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.byy.dal.entity.beans.cartitem.CartItem;
import com.byy.dal.entity.dos.cartitem.CartItemDO;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;

/**
 * @author: goblin
 * @date: 2019-06-17 15:26:38
 */
public interface CartItemMapper extends BaseMapper<CartItem> {

  /**
   * 小程序分页查询我的购物车
   */
  IPage<CartItemDO> pageByCartItem(Page page, @Param("userId") Long userId);

  /**
   * 批量查询所有选择商品信息
   */
  List<CartItemDO> listByCartItemIds(@Param("ids") List<Long> ids);
}

