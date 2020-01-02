package com.byy.api.security.filter;

import com.byy.api.response.AuthTypeJwtInfo;
import com.byy.api.security.check.TokenCheckService;
import com.byy.dal.common.utils.helper.Md5Helper;
import com.byy.dal.entity.beans.security.Jwt;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Nonnull;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.byy.dal.common.utils.helper.JsonHelper.obj2Str;

/**
 * 登录主入口
 *
 * @author: yyc
 * @date: 19-6-10 上午9:51
 */
public class JwtAuthorizationFilter extends OncePerRequestFilter {

  private final Jwt jwt;
  private final TokenCheckService tokenCheckService;

  public JwtAuthorizationFilter(Jwt jwt, TokenCheckService tokenCheckService) {
    this.jwt = jwt;
    this.tokenCheckService = tokenCheckService;
  }

  /**
   * 在该方法中校验token是否正确, 正确则放行, 否则拦截
   *
   * @param request HttpServletRequest
   * @param response HttpServletResponse
   * @param chain FilterChain
   * @throws ServletException
   * @throws IOException
   */
  @Override
  protected void doFilterInternal(
      @Nonnull HttpServletRequest request,
      @Nonnull HttpServletResponse response,
      @Nonnull FilterChain chain)
      throws ServletException, IOException {
    String testToken = testToken();
    tokenCheckService.checkAutoLogin(request, jwt.getHeader());
    chain.doFilter(request, response);
  }

  // 生成测试token
  private String testToken() {
    String tokenJson =
        obj2Str(new AuthTypeJwtInfo(2L, "wechat", Md5Helper.md5("jwtSecureKey" + 2L + "wechat")));
    return JwtTokenHelper.instance().generate(tokenJson);
  }
}
