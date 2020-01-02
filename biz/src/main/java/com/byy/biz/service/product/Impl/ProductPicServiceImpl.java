package com.byy.biz.service.product.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.byy.biz.service.product.ProductPicService;
import com.byy.dal.entity.beans.product.ProductPic;
import com.byy.dal.mapper.product.ProductPicMapper;
import org.springframework.stereotype.Service;

/**
 * @Author: xcf
 * @Date: 12/06/19 下午 02:36
 * @Description:
 */
@Service
public class ProductPicServiceImpl extends ServiceImpl<ProductPicMapper, ProductPic> implements ProductPicService {
}
