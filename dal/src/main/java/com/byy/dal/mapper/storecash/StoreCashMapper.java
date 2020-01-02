package com.byy.dal.mapper.storecash;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.byy.dal.entity.beans.storecash.StoreCash;
import com.byy.dal.entity.dos.storecash.StoreCashDO;
import java.util.Map;
import org.apache.ibatis.annotations.Param;

/**
 * @author: goblin
 * @date: 2019-06-27 15:44:29
 */
public interface StoreCashMapper extends BaseMapper<StoreCash> {

  /**
   * 后台分页条件查询
   */
  IPage<StoreCashDO> listBySearch(Page page, @Param("params") Map<String, Object> params);
}

