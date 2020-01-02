package com.byy.biz.service.location;

import com.baomidou.mybatisplus.extension.service.IService;
import com.byy.dal.entity.beans.location.AddressChain;

/**
 * @author: yyc
 * @date: 19-4-28 下午6:40
 */
public interface AddressChainService extends IService<AddressChain> {

  /**
   * 根据areaId查询省市区
   *
   * @param areaId Long
   * @return AddressChain
   */
  AddressChain loadAddressChain(Long areaId);
}
