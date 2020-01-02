package com.byy.dal.entity.beans.cash;

import com.byy.dal.entity.beans.base.BaseEntityArchiveWithUserId;
import com.byy.dal.enums.CashType;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.type.Alias;

/**
 * @program: flash-car
 * @description: 用户提现
 * @author: Goblin
 * @create: 2019-06-14 16:48
 **/
@Alias("Cash")
@Getter
@Setter
@ToString
public class Cash extends BaseEntityArchiveWithUserId {

  /**
   * 提现状态
   */
  private CashType status;
  /**
   * 提现额
   */
  private BigDecimal cashAmount;

  /**
   * 开户银行
   */
  private String bank;
  /**
   * 提现账号
   */
  private String cashAmountNumber;
  /**
   * 提现申请人
   */
  private String cashName;
}
