package com.byy.biz.service.product.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.byy.biz.service.product.SkuService;
import com.byy.dal.entity.beans.product.Sku;
import com.byy.dal.mapper.product.SkuMapper;
import org.springframework.stereotype.Service;

/**
 * @Author: xcf
 * @Date: 12/06/19 下午 02:43
 * @Description:
 */
@Service
public class SkuServiceImpl extends ServiceImpl<SkuMapper, Sku> implements SkuService {
}
