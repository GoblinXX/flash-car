package com.byy.dal.entity.beans.sys;

import com.byy.dal.entity.beans.base.BaseEntityArchiveWithUserId;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 后台用户权限
 *
 * @author: yyc
 * @date: 19-6-10 下午5:23
 */
@Setter
@Getter
@ToString
public class SysAuthority extends BaseEntityArchiveWithUserId {

  /** 类别菜单(唯一) */
  private String menuName;
}
