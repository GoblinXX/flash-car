package com.byy.dal.entity.beans.sys;

import com.byy.dal.entity.beans.base.BaseUser;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 后台用户
 *
 * @author: yyc
 * @date: 19-5-15 上午11:55
 */
@Setter
@Getter
@ToString
public class SysUser extends BaseUser {

  /** 用户姓名 */
  private String name;

  /** 门店id,为0代表总后台 */
  private Long storeId;
}
