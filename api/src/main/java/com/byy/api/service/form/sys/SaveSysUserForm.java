package com.byy.api.service.form.sys;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

/**
 * @author: yyc
 * @date: 19-5-15 下午3:54
 */
@Setter
@Getter
@ToString
public class SaveSysUserForm extends SysUserForm {

  @NotBlank private String username;

  @NotBlank private String password;
}
