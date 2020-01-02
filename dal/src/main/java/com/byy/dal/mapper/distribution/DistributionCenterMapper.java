package com.byy.dal.mapper.distribution;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.byy.dal.entity.beans.distribution.DistributionCenter;
import com.byy.dal.entity.dos.distribution.DistributionCenterDO;
import org.apache.ibatis.annotations.Param;

/**
 * @program: flash-car
 * @description: 分销中心mapper
 * @author: Goblin
 * @create: 2019-06-13 15:18
 **/
public interface DistributionCenterMapper extends BaseMapper<DistributionCenter> {

  /**
   * 小程序查询个人分销信息
   */
  DistributionCenterDO selectByDisId(@Param("id") Long id);
}
