package com.byy.dal.mapper.coupon;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.byy.dal.entity.beans.coupon.UserCoupon;
import com.byy.dal.entity.dos.coupon.UserCouponDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @Author: xcf
 * @Date: 15/06/19 下午 02:00
 * @Description:
 */
public interface UserCouponMapper extends BaseMapper<UserCoupon> {
  /**
   * 小程序端查询自己的优惠券
   * @param page
   * @param userId
   * @return
   */
  IPage<UserCouponDO> getOwnCouponApp(@Param("page") IPage page,@Param("userId") Long userId);
  /**
   * 小程序端查询自己可用的优惠券
   * @param page
   * @param params
   * @return
   */
  IPage<UserCouponDO> getOwnUsableCouponApp(@Param("page") IPage page,@Param("params") Map<String, Object> params);

  /**
   * 查询我的可用优惠券
   * @param params Map
   * @return List
   */
  List<UserCouponDO> selectCanUseCoupons(@Param("params") Map<String, Object> params);
}
