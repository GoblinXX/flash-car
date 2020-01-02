package com.byy.api.service.form.coupon;

import com.byy.api.service.form.page.IPageForm;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author: xcf
 * @Date: 15/06/19 下午 02:04
 * @Description:
 */
@Getter
@Setter
@ToString
public class CouponForm implements IPageForm {
  /** 当前页(从1开始) */
  @NotNull private Long page = 1L;
  /** 分页大小 */
  @NotNull private Long size = 8L;
  /** 姓名 */
  private String name;
  /** 面值 */
  private BigDecimal faceValue;
  /** 数量 */
  private Integer amount;
  /** 开始时间 */
  private LocalDateTime validFrom;
  /** 结束时间 */
  private LocalDateTime validTo;
  /** 使用条件 */
  private BigDecimal conditionUse;
}
