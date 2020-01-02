package com.byy.biz.service.commission.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.byy.biz.service.commission.CommissionRecordsService;
import com.byy.dal.entity.beans.commission.CommissionRecords;
import com.byy.dal.entity.dos.commission.CommissionRecordsDO;
import com.byy.dal.mapper.commission.CommissionRecordsMapper;
import org.springframework.stereotype.Service;

/**
 * @program: flash-car
 * @description: 佣金记录实现类
 * @author: Goblin
 * @create: 2019-06-14 10:34
 **/
@Service
public class CommissionRecordsServiceImpl extends
    ServiceImpl<CommissionRecordsMapper, CommissionRecords> implements
    CommissionRecordsService {

  @Override
  public IPage<CommissionRecordsDO> listBySearch(Page page, Long userId) {
    return baseMapper.listBySearch(page, userId);
  }
}
