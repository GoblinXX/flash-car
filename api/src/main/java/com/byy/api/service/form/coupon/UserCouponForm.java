package com.byy.api.service.form.coupon;

import com.byy.api.service.form.page.IPageForm;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

/**
 * @Author: xcf
 * @Date: 15/06/19 下午 02:05
 * @Description:
 */
@Getter
@Setter
@ToString
public class UserCouponForm implements IPageForm {
  /** 当前页(从1开始) */
  @NotNull private Long page = 1L;
  /** 分页大小 */
  @NotNull private Long size = 8L;


}
