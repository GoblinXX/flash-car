package com.byy.dal.entity.beans.distribution;

import com.byy.dal.entity.beans.base.BaseEntityArchiveWithUserId;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.type.Alias;

/**
 * @program: flash-car
 * @description: 分销中心
 * @author: Goblin
 * @create: 2019-06-13 11:57
 **/
@Alias("DistributionCenter")
@Setter
@Getter
@ToString
public class DistributionCenter extends BaseEntityArchiveWithUserId {

  /**
   * 上级id
   */
  private Long superiorId;
  /**
   * 累计佣金
   */
  private BigDecimal accumulatedCommission;
  /**
   * 当前佣金
   */
  private BigDecimal currentBalance;
}
