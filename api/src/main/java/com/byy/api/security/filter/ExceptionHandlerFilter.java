package com.byy.api.security.filter;

import com.byy.dal.entity.beans.security.SecurityProperties;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

/**
 * 在JWT过滤链之前,全局捕获过滤链异常
 *
 * @see JwtAuthorizationFilter
 * @see com.byy.api.config.SecurityConfig
 * @author: yyc
 * @date: 19-3-31 下午7:40
 */
@Slf4j
public class ExceptionHandlerFilter implements Filter {

  private final SecurityProperties securityProperties;

  public ExceptionHandlerFilter(SecurityProperties securityProperties) {
    this.securityProperties = securityProperties;
  }

  @Override
  public void init(FilterConfig filterConfig) {
    log.info("exceptionHandlerFilter init success...");
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    try {
      chain.doFilter(request, response);
    } catch (Throwable e) {
      log.error("异常filter捕获:", e);
      request.getRequestDispatcher(securityProperties.getLoginFailUrl()).forward(request, response);
    }
  }

  @Override
  public void destroy() {}
}
