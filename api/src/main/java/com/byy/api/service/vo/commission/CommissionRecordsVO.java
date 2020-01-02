package com.byy.api.service.vo.commission;

import com.byy.dal.enums.CommissionType;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @program: flash-car
 * @description: 佣金记录VO
 * @author: Goblin
 * @create: 2019-06-14 11:45
 **/
@Getter
@Setter
@ToString
public class CommissionRecordsVO {

  /**
   * 主键id
   */
  private Long id;
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
  /**
   * 创建时间
   */
  private LocalDateTime createdAt;
  /**
   * 下级昵称
   */
  private String nickname;
}
