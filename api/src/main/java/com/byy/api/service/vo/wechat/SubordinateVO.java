package com.byy.api.service.vo.wechat;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @program: flash-car
 * @description: 下级列表
 * @author: Goblin
 * @create: 2019-07-05 11:59
 **/
@Setter
@Getter
@ToString
public class SubordinateVO {

  /**
   * userId
   */
  private Long id;
  /**
   * 下级用户nickname
   */
  private String nickname;
  /**
   * 下级用户phone
   */
  private String phone;
  /**
   * 下级总消费金额
   */
  private BigDecimal totalPaid;
  /**
   * 用户头像
   */
  private String avatar;
}
