package com.byy.api.security.filter;

import com.byy.api.response.AuthTypeJwtInfo;
import com.byy.dal.common.errors.UserAuthException;
import com.byy.dal.common.utils.helper.CheckHelper;
import com.byy.dal.common.utils.helper.Md5Helper;
import com.byy.dal.entity.beans.base.BaseUser;
import com.byy.dal.entity.beans.security.Jwt;
import com.byy.dal.entity.beans.security.SecurityProperties;
import com.byy.dal.enums.AuthType;
import com.byy.dal.enums.GlobalErrorCode;
import org.apache.logging.log4j.util.Strings;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.ForwardAuthenticationFailureHandler;
import org.springframework.security.web.authentication.ForwardAuthenticationSuccessHandler;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import static com.byy.dal.common.utils.helper.JsonHelper.obj2Str;

/**
 * 通过jwt token实现的登录provider
 *
 * @author: yyc
 * @date: 19-3-29 下午2:24
 */
public abstract class JwtTokenBasedAuthenticationFilter
    extends AbstractAuthenticationProcessingFilter {

  static final String DEFAULT_PWD = "e10adc3949ba59abbe56e057f20f883e";

  /** 加密前缀 */
  private String secure;

  /** token */
  private String header;

  /**
   * 使用默认登录路径的构造器
   *
   * @param authenticationManager 登录19-3-31验证管理器
   * @param securityProperties 相关属性配置
   */
  JwtTokenBasedAuthenticationFilter(
      AuthenticationManager authenticationManager, SecurityProperties securityProperties) {
    this(securityProperties.getLoginUrl(), authenticationManager, securityProperties);
  }

  /**
   * 构造函数, 设置了部分默认值
   *
   * @param defaultFilterProcessUrl 默认处理的路径
   * @param authenticationManager 登录验证管理器
   * @param securityProperties 相关属性配置
   */
  JwtTokenBasedAuthenticationFilter(
      String defaultFilterProcessUrl,
      AuthenticationManager authenticationManager,
      SecurityProperties securityProperties) {
    super(defaultFilterProcessUrl);
    Jwt jwt = securityProperties.getJwt();
    this.secure = jwt.getSecure();
    this.header = jwt.getHeader();
    // 配置好默认登录成功地址
    setAuthenticationSuccessHandler(
        new ForwardAuthenticationSuccessHandler(securityProperties.getLoginSuccessUrl()));
    // 配置默认登录失败处理地址
    setAuthenticationFailureHandler(
        new ForwardAuthenticationFailureHandler(securityProperties.getLoginFailUrl()));
    setAuthenticationManager(authenticationManager);
  }

  /**
   * 子类实现提供登录token, 与相应的provider保持一致
   *
   * @param request req
   * @param response rep
   * @return Authentication
   * @throws AuthenticationException
   */
  @Override
  public Authentication attemptAuthentication(
      HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
    if (!Objects.equals(HttpMethod.POST.name(), request.getMethod())) {
      throw new AuthenticationServiceException(
          "Authentication method not supported: " + request.getMethod());
    }
    return this.getAuthenticationManager().authenticate(obtainAuthToken(request));
  }

  /**
   * 复写验证成功方法 将token放到header中
   *
   * @param request 当次request
   * @param response 当次response
   * @param chain 过滤器链
   * @param authResult 成功后的用户验证信息
   */
  @Override
  protected void successfulAuthentication(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain chain,
      Authentication authResult)
      throws IOException, ServletException {
    Long userId = ((BaseUser) authResult.getPrincipal()).getId();
    String userType = authScope().getDesc();
    String signature = sign(String.valueOf(userId), userType);
    String tokenJson = obj2Str(new AuthTypeJwtInfo(userId, userType, signature));
    // 生成token
    String token = JwtTokenHelper.instance().generate(tokenJson);
    response.addHeader(header, token);
    // 放入用户上下文
    SecurityContextHolder.getContext().setAuthentication(authResult);
    AuthenticationSuccessHandler successHandler = getSuccessHandler();
    if (successHandler != null) {
      // 放入token后执行successHandler, 请求分发到对应的处理器
      successHandler.onAuthenticationSuccess(request, response, authResult);
    }
  }

  /**
   * 从request中获取请求token表单 不同的实现类可以自己实现不同的表单类型, 在provider中处理
   *
   * @param request re
   * @return Authentication
   */
  protected abstract Authentication obtainAuthToken(HttpServletRequest request);

  /**
   * 获取登录参数
   *
   * @param request
   * @return String
   */
  String obtainJsonParams(HttpServletRequest request) {
    StringBuilder params = new StringBuilder();
    String inputStr;
    try (BufferedReader streamReader =
        new BufferedReader(
            new InputStreamReader(request.getInputStream(), StandardCharsets.UTF_8))) {
      while ((inputStr = streamReader.readLine()) != null) {
        params.append(inputStr);
      }
      return params.toString();
    } catch (Exception e) {
      throw new UserAuthException("解析json参数失败");
    }
  }

  @Override
  protected boolean requiresAuthentication(
      HttpServletRequest request, HttpServletResponse response) {
    String uri = request.getRequestURI();
    String authType = uri.substring(uri.lastIndexOf("/") + 1);
    return authScope().getDesc().equals(authType);
  }

  /**
   * 认证类型
   *
   * @return AuthType
   */
  protected abstract AuthType authScope();

  /**
   * 加密，用作跨域时认证
   *
   * @param userId u
   * @param userType t
   * @return String
   */
  private String sign(String userId, String userType) {
    return Md5Helper.md5(secure + userId + userType);
  }

}
