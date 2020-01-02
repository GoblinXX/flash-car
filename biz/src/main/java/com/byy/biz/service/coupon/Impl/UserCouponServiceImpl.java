package com.byy.biz.service.coupon.Impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.byy.biz.service.coupon.UserCouponService;
import com.byy.dal.entity.beans.coupon.UserCoupon;
import com.byy.dal.entity.dos.coupon.UserCouponDO;
import com.byy.dal.mapper.coupon.UserCouponMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Author: xcf
 * @Date: 15/06/19 下午 02:03
 * @Description:
 */
@Service
public class UserCouponServiceImpl extends ServiceImpl<UserCouponMapper, UserCoupon> implements UserCouponService {
  @Autowired
  private UserCouponMapper userCouponMapper;

  @Override
  public IPage<UserCouponDO> getOwnCouponApp(IPage page,Long userId) {
    return userCouponMapper.getOwnCouponApp(page,userId);
  }

  @Override
  public IPage<UserCouponDO> getOwnUsableCouponApp(IPage page, Map<String, Object> params) {
    return userCouponMapper.getOwnUsableCouponApp(page,params);
  }

  @Override
  public List<UserCouponDO> loadCanUseCoupons(Map<String, Object> params) {
    return userCouponMapper.selectCanUseCoupons(params);
  }
}
