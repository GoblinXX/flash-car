package com.byy.api.config;

import com.byy.api.security.check.TokenCheckService;
import com.byy.api.security.filter.ExceptionHandlerFilter;
import com.byy.api.security.filter.JwtAuthorizationFilter;
import com.byy.api.security.filter.JwtTokenBasedAuthenticationFilter;
import com.byy.api.security.filter.JwtTokenHelper;
import com.byy.api.security.filter.PhoneUserJwtAuthenticationFilter;
import com.byy.api.security.filter.SysUserJwtAuthenticationFilter;
import com.byy.api.security.filter.WeChatJwtAuthenticationFilter;
import com.byy.api.security.service.UserDetailsFactory;
import com.byy.api.security.wechat.WeChatApiService;
import com.byy.api.service.entry.JwtAuthenticationEntryPoint;
import com.byy.biz.service.point.UserPointService;
import com.byy.biz.service.sys.SysUserService;
import com.byy.biz.service.wechat.WeChatConfigService;
import com.byy.biz.service.wechat.WeChatUserService;
import com.byy.dal.entity.beans.security.Jwt;
import com.byy.dal.entity.beans.security.SecurityProperties;
import com.byy.dal.enums.AuthType;
import com.byy.oss.provider.CosProvider;
import com.google.common.collect.ImmutableList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.client.RestTemplate;

/**
 * @author: yyc
 * @date: 19-3-29 下午1:07
 */
@SpringBootConfiguration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  private final WeChatConfigService weChatConfigService;
  private final WeChatUserService weChatUserService;
  private final SysUserService sysUserService;
  private final RestTemplate restTemplate;
  private final PasswordEncoder passwordEncoder;
  private final UserDetailsFactory userDetailsFactory;
  private final CosProvider cosProvider;
  private final UserPointService userPointService;

  @Autowired
  public SecurityConfig(
      WeChatConfigService weChatConfigService,
      WeChatUserService weChatUserService,
      SysUserService sysUserService,
      RestTemplate restTemplate,
      PasswordEncoder passwordEncoder,
      UserDetailsFactory userDetailsFactory, CosProvider cosProvider,UserPointService userPointService) {
    this.weChatConfigService = weChatConfigService;
    this.weChatUserService = weChatUserService;
    this.sysUserService = sysUserService;
    this.restTemplate = restTemplate;
    this.passwordEncoder = passwordEncoder;
    this.userDetailsFactory = userDetailsFactory;
    this.cosProvider = cosProvider;
    this.userPointService = userPointService;
  }

  /**
   * 注入jwt配置
   *
   * @return SecurityProperties
   */
  @Bean
  @ConfigurationProperties(prefix = "security")
  public SecurityProperties securityProperties() {
    return new SecurityProperties();
  }

  /**
   * 注入jwtTokenHelper类, 同时初始化tokenHelper的静态单例对象
   *
   * @return JwtTokenHelper对象
   */
  @Bean
  public JwtTokenHelper jwtTokenHelper() {
    Jwt jwt = securityProperties().getJwt();
    return JwtTokenHelper.withConfig(jwt);
  }

  /**
   * 微信登录拦截器
   *
   * @return JwtTokenBasedAuthenticationFilter
   */
  @Bean("weChatJwtAuthenticationFilter")
  public JwtTokenBasedAuthenticationFilter weChatJwtAuthenticationFilter() {
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
    provider.setUserDetailsService(userDetailsFactory.getUserDetailsType(AuthType.WECHAT));
    provider.setPasswordEncoder(passwordEncoder);
    return new WeChatJwtAuthenticationFilter(
        new ProviderManager(ImmutableList.of(provider)),
        securityProperties(),
        weChatServiceApi(),
        weChatUserService,
        userPointService);
  }

  /**
   * 后台用户密码登录拦截器
   *
   * @return JwtTokenBasedAuthenticationFilter
   */
  @Bean("sysUserJwtAuthenticationFilter")
  public JwtTokenBasedAuthenticationFilter sysUserJwtAuthenticationFilter() {
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
    provider.setUserDetailsService(userDetailsFactory.getUserDetailsType(AuthType.SYS));
    provider.setPasswordEncoder(passwordEncoder);
    return new SysUserJwtAuthenticationFilter(
        new ProviderManager(ImmutableList.of(provider)), securityProperties(), sysUserService);
  }

  /**
   * 手机密码登录拦截器
   *
   * @return JwtTokenBasedAuthenticationFilter
   */
  @Bean("phoneUserJwtAuthenticationFilter")
  public JwtTokenBasedAuthenticationFilter phoneUserJwtAuthenticationFilter() {
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
    provider.setUserDetailsService(userDetailsFactory.getUserDetailsType(AuthType.PHONE));
    provider.setPasswordEncoder(passwordEncoder);
    return new PhoneUserJwtAuthenticationFilter(
        new ProviderManager(ImmutableList.of(provider)), securityProperties(), weChatUserService);
  }

  /**
   * 微信登录模板
   *
   * @return WeChatApiService
   */
  @Bean("weChatServiceApi")
  public WeChatApiService weChatServiceApi() {
    return new WeChatApiService(restTemplate, weChatConfigService, weChatUserService, cosProvider);
  }

  /**
   * token相关处理
   *
   * @return TokenCheckService
   */
  @Bean
  public TokenCheckService tokenCheckService() {
    return new TokenCheckService(userDetailsFactory);
  }

  /**
   * 注入基于jwtToken的校验filter
   *
   * @return jwtToken校验filter实例
   */
  @Bean
  public JwtAuthorizationFilter jwtAuthorizationFilter() {
    Jwt jwt = securityProperties().getJwt();
    return new JwtAuthorizationFilter(jwt, tokenCheckService());
  }

  /**
   * 注入全局filter异常处理
   *
   * @return ExceptionHandlerFilter
   */
  @Bean
  public ExceptionHandlerFilter exceptionHandlerFilter() {
    return new ExceptionHandlerFilter(securityProperties());
  }

  /**
   * 配置安全设置 默认的登录路径为/users/auth/login
   *
   * @param http 安全配置对象
   * @throws Exception e
   */
  @Override
  protected void configure(HttpSecurity http) throws Exception {
    String[] permitUrls = securityProperties().getPermitUrls();
    http.cors()
        .and()
        .csrf()
        .disable()
        .authorizeRequests()
        .antMatchers(HttpMethod.POST, permitUrls)
        .permitAll()
        .anyRequest()
        .authenticated()
        .and()
        .addFilterBefore(
            phoneUserJwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
        .addFilterBefore(
            weChatJwtAuthenticationFilter(), PhoneUserJwtAuthenticationFilter.class)
        .addFilterBefore(sysUserJwtAuthenticationFilter(), WeChatJwtAuthenticationFilter.class)
        .addFilterBefore(jwtAuthorizationFilter(), SysUserJwtAuthenticationFilter.class)
        // 配置过滤链异常处理器
        .addFilterBefore(exceptionHandlerFilter(), JwtAuthorizationFilter.class)
        .exceptionHandling()
        // 配置entry point处理拦截器登录异常
        .authenticationEntryPoint(new JwtAuthenticationEntryPoint(securityProperties()))
        .and()
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
  }
}
