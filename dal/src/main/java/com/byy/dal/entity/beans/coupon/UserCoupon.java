package com.byy.dal.entity.beans.coupon;

import com.byy.dal.entity.beans.base.BaseEntityArchiveWithUserId;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.type.Alias;

/**
 * @Author: xcf
 * @Date: 15/06/19 下午 01:53
 * @Description:用户优惠券类
 */
@Setter
@Getter
@ToString
@Alias("UserCoupon")
public class UserCoupon extends BaseEntityArchiveWithUserId {

  /** 优惠券id */
  private Long couponId;

  /** 是否可用 */
  private Boolean usable;
}
