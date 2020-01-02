package com.byy.api.security.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.byy.dal.common.errors.UserAuthException;
import com.byy.dal.entity.beans.security.Jwt;
import com.byy.dal.enums.GlobalErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;

import java.util.Date;
import java.util.Objects;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author: yyc
 * @date: 19-3-29 下午1:24
 */
@Slf4j
public class JwtTokenHelper {

  private static volatile JwtTokenHelper instance;

  private final Jwt jwt;

  private JwtTokenHelper(Jwt jwt) {
    this.jwt = jwt;
    instance = this;
  }

  /**
   * 通过static方法构造对象, 一般来说项目中只会有一个jwt配置对象 在tokenHelper构造函数中会初始化全局的instance, 在配置对象想等的情况下可以保证单例
   *
   * @param jwt 配置对象
   * @return tokenHelper实例
   */
  public static JwtTokenHelper withConfig(Jwt jwt) {
    checkNotNull(jwt);
    if (instance != null && Objects.equals(jwt, instance.jwt)) {
      return instance;
    }
    return new JwtTokenHelper(jwt);
  }

  /**
   * 在确保单例对象已初始化过后可以直接调用该方法获取全局对象
   *
   * @return 被缓存的全局对象
   * @throws IllegalStateException 对象尚未初始化
   */
  public static JwtTokenHelper instance() {
    if (instance == null) {
      throw new IllegalStateException("instance not initialized, please call withConfig first");
    }
    return instance;
  }

  /**
   * 生产token
   *
   * @param target 目标对象的字符串表示
   * @return 生产的token字符串
   */
  public String generate(String target) {
    return JWT.create()
        .withSubject(target)
        .withExpiresAt(new Date(System.currentTimeMillis() + jwt.getExpireTime()))
        .sign(HMAC512(jwt.getSecure().getBytes()));
  }

  /**
   * 从token中还原数据
   *
   * @param token 目标token
   * @return 还原的对象字符串
   * @throws AccessDeniedException 如果token不合法或已过期
   */
  public String extract(String token) {
    String ret;
    try {
      ret =
          JWT.require(Algorithm.HMAC512(jwt.getSecure().getBytes()))
              .build()
              .verify(token)
              .getSubject();
    } catch (AlgorithmMismatchException
        | SignatureVerificationException
        | InvalidClaimException e) {
      log.error("token {} 解密失败", token, e);
      throw new UserAuthException(GlobalErrorCode.INVALID_TOKEN_FORMAT);
    } catch (TokenExpiredException e) {
      log.error("token {} 已过期", e);
      throw new UserAuthException(GlobalErrorCode.TOKEN_EXPIRED);
    }
    return ret;
  }
}
