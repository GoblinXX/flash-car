package com.byy.biz.service.order.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.byy.biz.service.order.BackOrderService;
import com.byy.dal.entity.beans.order.Order;
import com.byy.dal.entity.dos.order.BackOrderDO;
import com.byy.dal.entity.dos.order.OrderDO;
import com.byy.dal.mapper.order.BackOrderMapper;
import com.byy.dal.mapper.order.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/** @Author: xcf @Date: 26/06/19 下午 02:14 @Description:后台订单 */
@Service
public class BackOrderServiceImpl extends ServiceImpl<BackOrderMapper, Order>
    implements BackOrderService {

  @Autowired private BackOrderMapper backOrderMapper;

  @Override
  public IPage<BackOrderDO> getOrder(IPage page, Map<String, Object> params) {
    return backOrderMapper.getOrder(page, params);
  }

  @Override
  public BackOrderDO getOrderById(Long orderId) {
    return backOrderMapper.getOrderById(orderId);
  }

  @Override
  public List<BackOrderDO> getOrderExport(Map<String, Object> params) {
    return backOrderMapper.getOrderExport(params);
  }
}
