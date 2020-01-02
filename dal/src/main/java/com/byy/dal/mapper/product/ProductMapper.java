package com.byy.dal.mapper.product;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.byy.dal.entity.beans.product.Product;
import com.byy.dal.entity.dos.product.ProductDO;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * @Author: xcf
 * @Date: 12/06/19 上午 09:53
 * @Description:商品mapper
 */
public interface ProductMapper extends BaseMapper<Product> {
  /**
   * 后台分页条件查询所有商品
   * @param page
   * @param params
   * @return
   */
  IPage<ProductDO> getAllProductSys(@Param("page") IPage page, @Param("params") Map<String, Object> params);

}
