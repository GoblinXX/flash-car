package com.byy.dal.mapper.store;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.byy.dal.entity.beans.store.Store;
import com.byy.dal.entity.dos.store.StoreDO;
import java.util.Map;
import org.apache.ibatis.annotations.Param;

/**
 * @program: flash-car
 * @description: 门店Mapper
 * @author: Goblin
 * @create: 2019-06-11 15:30
 **/
public interface StoreMapper extends BaseMapper<Store> {

  /**
   * 后台条件分页查询门店列表
   */
  IPage<StoreDO> listBySearch(Page page, @Param("params") Map<String, Object> params);

  /**
   * 小程序端分页查询门店
   * @return
   */
  IPage<Store> getStoreToHome(Page page);
}
