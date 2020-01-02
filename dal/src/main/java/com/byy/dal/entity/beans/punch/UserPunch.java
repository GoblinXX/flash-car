package com.byy.dal.entity.beans.punch;

import com.byy.dal.entity.beans.base.BaseEntityArchiveWithUserId;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.type.Alias;

import java.time.LocalDateTime;

/**
 * @Author: xcf
 * @Date: 14/06/19 下午 02:03
 * @Description:用户签到类
 */
@Getter
@Setter
@ToString
@Alias("UserPunch")
public class UserPunch extends BaseEntityArchiveWithUserId {

  /** 连续打卡天数 */
  private Integer accumulatedDays;

  /** 签到日期 */
  private LocalDateTime punchTime;
}
