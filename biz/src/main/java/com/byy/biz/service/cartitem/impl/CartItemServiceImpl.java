package com.byy.biz.service.cartitem.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.byy.dal.entity.beans.cartitem.CartItem;
import com.byy.dal.entity.dos.cartitem.CartItemDO;
import com.byy.dal.mapper.cartitem.CartItemMapper;
import com.byy.biz.service.cartitem.CartItemService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author: goblin
 * @date: 2019-06-17 15:26:38
 */
@Service
public class CartItemServiceImpl extends ServiceImpl<CartItemMapper, CartItem>
    implements CartItemService {

  @Autowired
  private CartItemMapper cartItemMapper;

  @Override
  public IPage<CartItemDO> pageByCartItem(Page page, Long userId) {
    return cartItemMapper.pageByCartItem(page, userId);
  }

  @Override
  public List<CartItemDO> listByCartItemIds(List<Long> ids) {
    return cartItemMapper.listByCartItemIds(ids);
  }
}
