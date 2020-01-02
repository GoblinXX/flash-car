package com.byy.dal.mapper.commission;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.byy.dal.entity.beans.commission.CommissionRecords;
import com.byy.dal.entity.dos.commission.CommissionRecordsDO;
import org.apache.ibatis.annotations.Param;

/**
 * @program: flash-car
 * @description: 佣金记录mapper
 * @author: Goblin
 * @create: 2019-06-14 10:33
 **/
public interface CommissionRecordsMapper extends BaseMapper<CommissionRecords> {

  /**
   * 小程序分页查询我的佣金明细
   */
  IPage<CommissionRecordsDO> listBySearch(Page page, @Param("userId") Long userId);
}
