package com.byy.api.security.service;

import com.byy.biz.service.wechat.WeChatUserService;
import com.byy.dal.common.errors.UserAuthException;
import com.byy.dal.common.utils.helper.CheckHelper;
import com.byy.dal.enums.AuthType;
import com.byy.dal.enums.GlobalErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author: Goblin
 * @date: 19-06-26 上午11:31
 */
@Service
public class PhoneUserDetailsService implements IUserDetailsService {

  private final WeChatUserService weChatUserService;

  @Autowired
  public PhoneUserDetailsService(
      WeChatUserService weChatUserService) {
    this.weChatUserService = weChatUserService;
  }

  @Override
  public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
    return CheckHelper.nonEmptyOrThrow(
        weChatUserService.getById(Long.parseLong(id)),
        UserAuthException::new,
        GlobalErrorCode.USER_AUTH_ERROR);
  }

  @Override
  public AuthType obtainAuthType() {
    return AuthType.PHONE;
  }
}
