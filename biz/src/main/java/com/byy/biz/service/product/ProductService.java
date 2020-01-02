package com.byy.biz.service.product;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.byy.dal.entity.beans.product.Product;
import com.byy.dal.entity.dos.product.ProductDO;

import java.util.Map;

/**
 * @Author: xcf
 * @Date: 12/06/19 上午 11:23
 * @Description:商品Service
 */
public interface ProductService extends IService<Product> {
  /**
   * 后台分页条件查询所有商品
   * @param page
   * @param params
   * @return
   */
  IPage<ProductDO> getAllProductSys(IPage page,Map<String, Object> params);

}
