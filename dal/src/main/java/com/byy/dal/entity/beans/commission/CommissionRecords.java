package com.byy.dal.entity.beans.commission;

import com.byy.dal.entity.beans.base.BaseEntityArchiveWithUserId;
import com.byy.dal.enums.CommissionType;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.type.Alias;

/**
 * @program: flash-car
 * @description: 佣金记录
 * @author: Goblin
 * @create: 2019-06-14 10:07
 **/
@Alias("CommissionRecords")
@Setter
@Getter
@ToString
public class CommissionRecords extends BaseEntityArchiveWithUserId {

  /**
   * 变化量
   */
  private BigDecimal num;
  /**
   * 订单编号
   */
  private String orderNo;
  /**
   * 类型
   */
  private CommissionType type;
  /**
   * 历史佣金
   */
  private BigDecimal beforeCommission;
  /**
   * 当前佣金
   */
  private BigDecimal afterCommission;
}
