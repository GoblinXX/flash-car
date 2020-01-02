package com.byy.api.security.check;

import com.byy.api.response.AuthTypeJwtInfo;
import com.byy.api.security.filter.JwtTokenHelper;
import com.byy.api.security.service.UserDetailsFactory;
import com.byy.dal.enums.AuthType;
import org.apache.logging.log4j.util.Strings;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.servlet.http.HttpServletRequest;

import static com.byy.dal.common.utils.helper.JsonHelper.str2Obj;

/**
 * token处理类
 *
 * @author: yyc
 * @date: 19-5-6 下午5:46
 */
public class TokenCheckService {

  private final UserDetailsFactory userDetailsFactory;

  public TokenCheckService(UserDetailsFactory userDetailsFactory) {
    this.userDetailsFactory = userDetailsFactory;
  }

  /**
   * 检查自动登录
   *
   * @param request HttpServletRequest
   * @param header String
   */
  public void checkAutoLogin(HttpServletRequest request, String header) {
    String token = request.getHeader(header);
    if (Strings.isNotBlank(token)) {
      String extract = JwtTokenHelper.instance().extract(token);
      AuthTypeJwtInfo authTypeJwtInfo = str2Obj(extract, AuthTypeJwtInfo.class);
      AuthType authType = AuthType.of(authTypeJwtInfo.getType());
      UserDetailsService userDetailsService = userDetailsFactory.getUserDetailsType(authType);
      UserDetails user =
          userDetailsService.loadUserByUsername(String.valueOf(authTypeJwtInfo.getId()));
      // 更新上下中的登录信息
      Authentication auth = new UsernamePasswordAuthenticationToken(user, user.getPassword(), null);
      SecurityContextHolder.getContext().setAuthentication(auth);
    }
  }
}
