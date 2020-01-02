package com.byy.api.security.service;

import com.byy.dal.enums.AuthType;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * @author: yyc
 * @date: 19-3-31 下午4:46
 */
public interface IUserDetailsService extends UserDetailsService {

  AuthType obtainAuthType();
}
