package com.byy.api.service.vo.cash;

import com.byy.dal.enums.CashType;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @program: flash-car
 * @description: 提现vo
 * @author: Goblin
 * @create: 2019-06-14 17:00
 **/
@Getter
@Setter
@ToString
public class CashVO {

  /**
   * 主键id
   */
  private Long id;
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
  /**
   * 提现时间
   */
  private LocalDateTime createdAt;
  /**
   * 昵称
   */
  private String nickname;

  /**
   * 头像
   */
  private String avatar;
  /**
   * 手机号
   */
  private String phone;
}
