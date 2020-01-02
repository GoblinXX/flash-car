package com.byy.biz.service.storecash;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.byy.dal.entity.beans.storecash.StoreCash;
import com.byy.dal.entity.dos.storecash.StoreCashDO;
import java.util.Map;

/**
 * @author: goblin
 * @date: 2019-06-27 15:44:29
 */
public interface StoreCashService extends IService<StoreCash> {

  /**
   * 后台分页条件查询
   */
  IPage<StoreCashDO> listBySearch(Page page, Map<String, Object> params);
}

