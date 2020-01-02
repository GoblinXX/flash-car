package com.byy.dal.mapper.roadrescueorder;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.byy.dal.entity.beans.roadrescueorder.RoadRescueOrder;
import com.byy.dal.entity.dos.home.HomeDO;
import com.byy.dal.entity.dos.roadrescueorder.RoadRescueOrderDO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;

/**
 * @author: goblin
 * @date: 2019-06-18 15:09:34
 */
public interface RoadRescueOrderMapper extends BaseMapper<RoadRescueOrder> {

  /**
   * 查询修改订单状态后的数据
   */
  RoadRescueOrderDO selectRoadRescue(@Param("id") Long id);

  /**
   * 小程序端分页条件查询我的道路救援订单
   */
  IPage<RoadRescueOrderDO> selectRoadRescueToApp(Page page,
      @Param("params") Map<String, Object> params);

  /**
   * 总/分后台分页条件查询
   */
  IPage<RoadRescueOrderDO> selectRoadRescueToBack(Page page,
      @Param("params") Map<String, Object> params);

  List<HomeDO> getRoadOrderToChart(@Param("storeId") Long storeId, @Param("startTime") LocalDate startTime, @Param("endTime")LocalDate endTime);
}

