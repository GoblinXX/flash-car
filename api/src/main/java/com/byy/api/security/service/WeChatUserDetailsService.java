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
 * @author: yyc
 * @date: 19-3-31 下午4:41
 */
@Service
public class WeChatUserDetailsService implements IUserDetailsService {

  private final WeChatUserService weChatUserService;

  @Autowired
  public WeChatUserDetailsService(WeChatUserService weChatUserService) {
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
    return AuthType.WECHAT;
  }
}
