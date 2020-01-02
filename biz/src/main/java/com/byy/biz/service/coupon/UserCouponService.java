package com.byy.biz.service.coupon;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.byy.dal.entity.beans.coupon.UserCoupon;
import com.byy.dal.entity.dos.coupon.UserCouponDO;

import java.util.List;
import java.util.Map;

/**
 * 我的优惠券
 *
 * @author: xcf
 * @date: 15/06/19 下午 02:01
 */
public interface UserCouponService extends IService<UserCoupon> {
  /**
   * 小程序端查询自己的优惠券
   *
   * @param page
   * @param userId
   * @return
   */
  IPage<UserCouponDO> getOwnCouponApp(IPage page,Long userId);

  /**
   * 小程序端查询自己可用的优惠券
   * @param page
   * @param params
   * @return
   */
  IPage<UserCouponDO> getOwnUsableCouponApp(IPage page, Map<String,Object> params);

  /**
   * 查询我的可用优惠券
   *
   * @param params Map
   * @return List
   */
  List<UserCouponDO> loadCanUseCoupons(Map<String, Object> params);
}