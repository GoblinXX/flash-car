package com.byy.biz.service.coupon.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.byy.biz.service.coupon.CouponService;
import com.byy.dal.entity.beans.coupon.Coupon;
import com.byy.dal.mapper.coupon.CouponMapper;
import org.springframework.stereotype.Service;

/**
 * @Author: xcf
 * @Date: 15/06/19 下午 02:02
 * @Description:
 */
@Service
public class CouponServiceImpl extends ServiceImpl<CouponMapper, Coupon> implements CouponService {
}
