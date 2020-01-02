package com.byy.dal.entity.beans.coupon;

import com.byy.dal.entity.beans.base.BaseEntityArchive;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.type.Alias;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author: xcf
 * @Date: 15/06/19 下午 01:49
 * @Description:优惠券类
 */
@Setter
@Getter
@ToString
@Alias("Coupon")
public class Coupon extends BaseEntityArchive {

  /** 姓名 */
  private String name;

  /** 面值 */
  private BigDecimal faceValue;

  /** 数量 */
  private Integer amount;

  /** 剩余数量 */
  private Integer restAmount;

  /** 开始时间 */
  private LocalDateTime validFrom;

  /** 结束时间 */
  private LocalDateTime validTo;

  /** 使用条件 */
  private BigDecimal conditionUse;

}
