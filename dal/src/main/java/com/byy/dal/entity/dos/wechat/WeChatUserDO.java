package com.byy.dal.entity.dos.wechat;

import com.byy.dal.entity.beans.wechat.WeChatUser;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.type.Alias;

/**
 * @author: yyc
 * @date: 19-4-26 上午11:55
 */
@Setter
@Getter
@ToString
@Alias("WeChatUserDO")
public class WeChatUserDO extends WeChatUser {

  /**
   * 可用积分
   */
  private BigDecimal availablePoint;

  /**
   * 累加积分(只加不减)
   */
  private BigDecimal cumulativePoint;
  /**
   * 累计佣金
   */
  private BigDecimal accumulatedCommission;
  /**
   * 当前佣金
   */
  private BigDecimal currentBalance;
  /**
   * 上级id
   */
  private String superiorId;
}
