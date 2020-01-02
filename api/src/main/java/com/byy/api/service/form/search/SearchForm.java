package com.byy.api.service.form.search;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * @author: yyc
 * @date: 19-6-10 上午9:44
 */
public interface SearchForm {

  /**
   * 关键字模糊搜索
   *
   * @return String
   */
  String getKeyWord();

  /**
   * 开始时间
   *
   * @return Long
   */
  Long getStartTime();

  /**
   * 结束时间
   *
   * @return Long
   */
  Long getEndTime();

  /**
   * 单时间查询
   *
   * @return Long
   */
  Long getTime();

  /**
   * 时间戳转化为日期
   *
   * @param time Long
   * @return LocalDateTime
   */
  default LocalDateTime of(Long time) {
    return null != time
        ? LocalDateTime.ofInstant(Instant.ofEpochMilli(time), ZoneId.of("+8"))
        : null;
  }

  /**
   * 获取开始时间参数
   *
   * @return LocalDataTime
   */
  default LocalDateTime getLocalStartTime() {
    return of(getStartTime());
  }

  /**
   * 获取结束时间参数
   *
   * @return LocalDataTime
   */
  default LocalDateTime getLocalEndTime() {
    return of(getEndTime());
  }

  /**
   * 获取时间参数
   *
   * @return LocalDataTime
   */
  default LocalDateTime getLocalTime() {
    return of(getTime());
  }
}
