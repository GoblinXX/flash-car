package com.byy.biz.service.store.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.byy.biz.service.store.StoreService;
import com.byy.dal.entity.beans.store.Store;
import com.byy.dal.entity.dos.store.StoreDO;
import com.byy.dal.mapper.store.StoreMapper;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @program: flash-car
 * @description: 门店service实现类
 * @author: Goblin
 * @create: 2019-06-11 15:32
 **/
@Service
public class StoreServiceImpl extends ServiceImpl<StoreMapper, Store> implements StoreService {

  @Autowired
  private StoreMapper storeMapper;

  @Override
  public IPage<StoreDO> listBySearch(Page page, Map<String, Object> params) {
    return storeMapper.listBySearch(page, params);
  }

  @Override
  public IPage<Store> getStoreToHome(Page page) {
    return storeMapper.getStoreToHome(page);
  }
}
