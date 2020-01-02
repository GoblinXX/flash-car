package com.byy.biz.service.product.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.byy.biz.service.product.RentProductTimeService;
import com.byy.dal.entity.beans.product.RentProductTime;
import com.byy.dal.mapper.product.RentProductTimeMapper;
import org.springframework.stereotype.Service;

/**
 * @Author: xcf
 * @Date: 13/06/19 下午 03:47
 * @Description:
 */
@Service
public class RentProductTimeServiceImpl extends ServiceImpl<RentProductTimeMapper, RentProductTime> implements RentProductTimeService {
}
