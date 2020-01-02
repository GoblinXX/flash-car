package com.byy.biz.service.product.Impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.byy.biz.service.product.RentProductService;
import com.byy.dal.entity.beans.product.RentProduct;
import com.byy.dal.entity.dos.product.RentProductDO;
import com.byy.dal.mapper.product.RentProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @Author: xcf
 * @Date: 13/06/19 下午 02:47
 * @Description:
 */
@Service
public class RentProductServiceImpl extends ServiceImpl<RentProductMapper, RentProduct> implements RentProductService {
}
