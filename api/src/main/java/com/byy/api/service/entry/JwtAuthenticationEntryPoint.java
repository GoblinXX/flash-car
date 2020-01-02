package com.byy.api.service.entry;

import com.byy.dal.entity.beans.security.SecurityProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.WebAttributes;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author: yyc
 * @date: 19-3-29 下午3:18
 */
@Slf4j
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

  private final SecurityProperties securityProperties;

  public JwtAuthenticationEntryPoint(SecurityProperties securityProperties) {
    this.securityProperties = securityProperties;
  }

  /**
   * 统一使用/user/auth/fail来处理登录异常
   *
   * @param request req
   * @param response res
   * @param authException auth
   * @throws IOException ioExp
   * @throws ServletException serExp
   */
  @Override
  public void commence(
      HttpServletRequest request,
      HttpServletResponse response,
      AuthenticationException authException)
      throws IOException, ServletException {
    log.error("请求URL: {} access denied", request.getServletPath());
    request.setAttribute(WebAttributes.AUTHENTICATION_EXCEPTION, authException);
    request.getRequestDispatcher(securityProperties.getLoginFailUrl()).forward(request, response);
  }
}
