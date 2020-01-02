package com.byy.biz.service.commission;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.byy.dal.entity.beans.commission.CommissionRecords;
import com.byy.dal.entity.dos.commission.CommissionRecordsDO;

/**
 * @program: flash-car
 * @description: 佣金记录service
 * @author: Goblin
 * @create: 2019-06-14 10:34
 **/
public interface CommissionRecordsService extends IService<CommissionRecords> {

  /**
   * 小程序分页查询我的佣金明细
   */
  IPage<CommissionRecordsDO> listBySearch(Page page, Long userId);
}
