package com.byy.dal.entity.dos.coupon;



import com.byy.dal.entity.beans.coupon.UserCoupon;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.type.Alias;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author: xcf
 * @Date: 15/06/19 下午 05:09
 * @Description:
 */
@Getter
@Setter
@ToString
@Alias("UserCouponDO")
public class UserCouponDO extends UserCoupon {

  /** 优惠券名称 */
  private String couponName;

  /** 面值 */
  private BigDecimal faceValue;

  /** 开始时间 */
  private LocalDateTime validFrom;

  /** 结束时间 */
  private LocalDateTime validTo;

  /** 使用条件 */
  private BigDecimal conditionUse;

  /** 优惠券删除状态 */
  private Boolean couponArchive;

}
