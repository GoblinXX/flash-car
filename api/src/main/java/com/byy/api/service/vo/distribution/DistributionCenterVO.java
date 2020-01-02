package com.byy.api.service.vo.distribution;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @program: flash-car
 * @description: 分销中心VO
 * @author: Goblin
 * @create: 2019-06-13 16:44
 **/
@Setter
@Getter
@ToString
public class DistributionCenterVO {

  /**
   * 主键id
   */
  private Long id;
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
  /**
   * 当前用户id
   */
  private Long userId;
  /**
   * 当前用户nickname
   */
  private String nickname;

  /**
   * 当前用户头像
   */
  private String avatar;
  /**
   * 上级nickname
   */
  private String superiorName;
  /**
   * 我的下级总数
   */
  private Integer total;


}
