package com.byy.biz.service.roadrescueorder;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.byy.dal.entity.beans.roadrescueorder.RoadRescueOrder;
import com.byy.dal.entity.dos.roadrescueorder.RoadRescueOrderDO;
import java.util.Map;

/**
 * @author: goblin
 * @date: 2019-06-18 15:09:34
 */
public interface RoadRescueOrderService extends IService<RoadRescueOrder> {

  /**
   * 查询修改订单状态后的数据
   */
  RoadRescueOrderDO selectRoadRescue(Long id);

  /**
   * 小程序端分页条件查询我的道路救援订单
   */
  IPage<RoadRescueOrderDO> selectRoadRescueToApp(
      Page page, Map<String, Object> params);

  /**
   * 总/分后台分页条件查询
   */
  IPage<RoadRescueOrderDO> selectRoadRescueToBack(Page page, Map<String, Object> params);

  /**
   * 支付回调
   *
   * @param orderNo String
   */
  void asyncPayBack(String orderNo);
  /**
   * 发送短信
   *
   * @param orderNo String
   */
  void sendSms(String orderNo);
}

