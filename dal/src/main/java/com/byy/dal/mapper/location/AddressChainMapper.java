package com.byy.dal.mapper.location;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.byy.dal.entity.beans.location.AddressChain;
import org.apache.ibatis.annotations.Param;

/**
 * @author: yyc
 * @date: 19-4-10 下午8:53
 */
public interface AddressChainMapper extends BaseMapper<AddressChain> {

  /**
   * 根据areaId查询省市区
   *
   * @param areaId Long
   * @return AddressChain
   */
  AddressChain selectAddressChain(@Param("areaId") Long areaId);
}
