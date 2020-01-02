package com.byy.biz.service.store;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.byy.dal.entity.beans.store.Store;
import com.byy.dal.entity.dos.store.StoreDO;
import java.util.Map;
import org.apache.ibatis.annotations.Param;

/**
 * @program: flash-car
 * @description: 门店service
 * @author: Goblin
 * @create: 2019-06-11 15:32
 **/
public interface StoreService extends IService<Store> {

  /**
   * 后台条件分页查询门店列表
   */
  IPage<StoreDO> listBySearch(Page page, Map<String, Object> params);

  /**
   * 小程序端分页查询
   * @param page
   * @return
   */
  IPage<Store> getStoreToHome(Page page);

}
