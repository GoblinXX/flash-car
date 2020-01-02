package com.byy.biz.service.distribution;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.byy.dal.entity.beans.distribution.DistributionCenter;
import com.byy.dal.entity.beans.order.Order;
import com.byy.dal.entity.dos.distribution.DistributionCenterDO;
import org.apache.ibatis.annotations.Param;

/**
 * @program: flash-car
 * @description: 分销中心service
 * @author: Goblin
 * @create: 2019-06-13 15:19
 **/
public interface DistributionCenterService extends IService<DistributionCenter> {

  /**
   * 小程序查询个人分销信息
   */
  DistributionCenterDO selectByDisId(Long id);

  /**
   * 计算返佣
   */
  void computationOfCommissionRebate(Order order);
}
