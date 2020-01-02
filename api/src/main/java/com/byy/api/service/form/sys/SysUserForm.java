package com.byy.api.service.form.sys;

import com.byy.dal.entity.beans.sys.SysUser;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @author: yyc
 * @date: 19-5-15 下午3:54
 */
@Setter
@Getter
@ToString
public class SysUserForm extends SysUser {

  /** 权限类型列表 */
  private List<String> sysAuthorities;
}
