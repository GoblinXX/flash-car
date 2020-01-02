package com.byy.biz.service.product.Impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.byy.biz.service.product.ProductService;
import com.byy.dal.entity.beans.product.Product;
import com.byy.dal.entity.dos.product.ProductDO;
import com.byy.dal.mapper.product.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @Author: xcf
 * @Date: 12/06/19 上午 11:25
 * @Description:
 */
@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

  @Autowired
  private ProductMapper productMapper;

  @Override
  public IPage<ProductDO> getAllProductSys(IPage page,Map<String, Object> params) {
    return productMapper.getAllProductSys(page,params);
  }

}
