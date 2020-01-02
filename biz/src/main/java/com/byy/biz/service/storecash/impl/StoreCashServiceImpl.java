package com.byy.biz.service.storecash.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.byy.biz.service.storecash.StoreCashService;
import com.byy.dal.entity.beans.storecash.StoreCash;
import com.byy.dal.entity.dos.storecash.StoreCashDO;
import com.byy.dal.mapper.storecash.StoreCashMapper;
import java.util.Map;
import org.springframework.stereotype.Service;

/**
 * @author: goblin
 * @date: 2019-06-27 15:44:29
 */
@Service
public class StoreCashServiceImpl extends ServiceImpl<StoreCashMapper, StoreCash>
    implements StoreCashService {

  @Override
  public IPage<StoreCashDO> listBySearch(Page page, Map<String, Object> params) {
    return baseMapper.listBySearch(page, params);
  }
}
