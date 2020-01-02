package com.byy.dal.entity.beans.point;

import com.byy.dal.entity.beans.base.BaseEntityArchiveWithUserId;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.type.Alias;

import java.math.BigDecimal;

/**
 * @Author: xcf
 * @Date: 14/06/19 下午 02:06
 * @Description:用户积分类
 */
@Getter
@Setter
@ToString
@Alias("UserPoint")
public class UserPoint extends BaseEntityArchiveWithUserId {

  /** 可用积分 */
  private BigDecimal availablePoint;

  /** 累加积分(只加不减) */
  private BigDecimal cumulativePoint;
}
