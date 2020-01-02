package com.byy.api.service.vo.wechat;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 用户
 *
 * @author: yyc
 * @date: 19-1-29 上午11:31
 */
@Setter
@Getter
@ToString
public class WeChatUserVO {

  /**
   * 主键id
   */
  private Long id;
  /**
   * 三方openId
   */
  private String openId;

  /**
   * 三方unionId
   */
  private String unionId;

  /**
   * 手机号
   */
  private String phone;

  /**
   * 性别(0是未知，1代表男，2代表女)
   */
  private Integer gender;

  /**
   * 昵称
   */
  private String nickname;

  /**
   * 头像
   */
  private String avatar;

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
   * 上级昵称
   */
  private String superiorName;
  /**
   * 上级id
   */
  private String superiorId;
}
