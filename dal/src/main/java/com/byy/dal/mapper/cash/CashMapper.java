package com.byy.dal.mapper.cash;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.byy.dal.entity.beans.cash.Cash;
import com.byy.dal.entity.dos.cash.CashDO;
import java.util.Map;
import org.apache.ibatis.annotations.Param;

/**
 * @program: flash-car
 * @description: 提现申请
 * @author: Goblin
 * @create: 2019-06-14 16:53
 **/
public interface CashMapper extends BaseMapper<Cash> {

  /**
   * 后台分页条件查询提现记录
   */
  IPage<CashDO> listBySearch(Page page, @Param("params") Map<String, Object> params);
}
