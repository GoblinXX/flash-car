package com.byy.api.security.service;

import com.byy.biz.service.sys.SysUserService;
import com.byy.dal.common.errors.UserAuthException;
import com.byy.dal.common.utils.helper.CheckHelper;
import com.byy.dal.entity.beans.sys.SysUser;
import com.byy.dal.enums.AuthType;
import com.byy.dal.enums.GlobalErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * @author: yyc
 * @date: 19-3-31 下午4:41
 */
@Service
public class SysUserDetailsService implements IUserDetailsService {

  private final SysUserService sysUserService;
  private final PasswordEncoder passwordEncoder;

  @Autowired
  public SysUserDetailsService(SysUserService sysUserService, PasswordEncoder passwordEncoder) {
    this.sysUserService = sysUserService;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
    SysUser sysUser =
        CheckHelper.nonEmptyOrThrow(
            sysUserService.getById(Long.parseLong(id)),
            UserAuthException::new,
            GlobalErrorCode.USER_AUTH_ERROR);
    sysUser.setPassword(passwordEncoder.encode(sysUser.getPassword()));
    return sysUser;
  }

  @Override
  public AuthType obtainAuthType() {
    return AuthType.SYS;
  }
}
