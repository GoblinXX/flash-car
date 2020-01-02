package com.byy.dal.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: xcf
 * @Date: 20/06/19 下午 02:12
 * @Description:审核状态枚举
 */
@Getter
@AllArgsConstructor
public enum ApprovalStatus {
  UNREVIEWED(1,"未审核"),
  PASSED(2,"已通过"),
  FAILED(3,"未通过");

  /** 状态编号 */
  private Integer statusCode;

  /** 状态名 */
  private String statusName;
}
