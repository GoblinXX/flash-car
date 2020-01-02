package com.byy.biz.service.cash;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.byy.dal.entity.beans.cash.Cash;
import com.byy.dal.entity.dos.cash.CashDO;
import java.util.Map;

/**
 * @program: flash-car
 * @description: 提现申请service
 * @author: Goblin
 * @create: 2019-06-14 16:54
 **/
public interface CashService extends IService<Cash> {

  /**
   * 后台分页条件查询提现记录
   */
  IPage<CashDO> listBySearch(Page page, Map<String, Object> params);
}
