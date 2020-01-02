package com.byy.api.security.service;

import com.byy.dal.enums.AuthType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: yyc
 * @date: 19-3-31 下午4:43
 */
@Service
public class UserDetailsFactory {

  private final List<IUserDetailsService> userDetails;

  @Autowired
  public UserDetailsFactory(List<IUserDetailsService> userDetails) {
    this.userDetails = userDetails;
  }

  public IUserDetailsService getUserDetailsType(AuthType authType) {
    for (IUserDetailsService userDetail : userDetails) {
      if (userDetail.obtainAuthType().equals(authType)) {
        return userDetail;
      }
    }
    return null;
  }
}
