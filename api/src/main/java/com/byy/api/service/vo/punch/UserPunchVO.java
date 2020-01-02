package com.byy.api.service.vo.punch;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * @Author: xcf
 * @Date: 14/06/19 下午 02:59
 * @Description:
 */
@Getter
@Setter
@ToString
public class UserPunchVO {

  /** 用户UserId */
  private Long userId;

  /** 连续打卡天数 */
  private Integer accumulatedDays;

  /** 签到日期 */
  private LocalDateTime punchTime;
}
