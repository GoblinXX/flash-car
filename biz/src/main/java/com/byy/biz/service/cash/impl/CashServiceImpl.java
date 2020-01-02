package com.byy.biz.service.cash.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.byy.biz.service.cash.CashService;
import com.byy.dal.entity.beans.cash.Cash;
import com.byy.dal.entity.dos.cash.CashDO;
import com.byy.dal.mapper.cash.CashMapper;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @program: flash-car
 * @description: 提现申请实现类
 * @author: Goblin
 * @create: 2019-06-14 16:54
 **/
@Service
public class CashServiceImpl extends ServiceImpl<CashMapper, Cash> implements CashService {

  @Autowired
  private CashMapper cashMapper;

  @Override
  public IPage<CashDO> listBySearch(Page page, Map<String, Object> params) {
    return cashMapper.listBySearch(page, params);
  }
}
